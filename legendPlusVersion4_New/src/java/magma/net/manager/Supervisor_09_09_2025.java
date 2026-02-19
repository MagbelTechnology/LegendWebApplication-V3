package magma.net.manager;

import com.magbel.util.*;

import java.sql.*;
import java.util.*;

import magma.net.vao.Requisition;
import magma.net.vao.Transaction;
import magma.net.dao.MagmaDBConnection;
import magma.net.vao.FleetTransaction;
import legend.admin.handlers.CompanyHandler;
import legend.admin.objects.Company;

import com.magbel.legend.bus.ApprovalRecords;

public class Supervisor_09_09_2025 extends MagmaDBConnection {
 ApprovalRecords aprecords = null;
    public Supervisor_09_09_2025() {
            aprecords = new ApprovalRecords();
	} 

	// get transaction status for user
	public ArrayList findTransactionStatusList(String query_,String userId) {
		System.out.println("=======query_ " + query_); 
		String SELECT_QUERY_OLD = "SELECT DISTINCT a.BATCH_ID,a.TRANSACTION_ID,a.DR_ACCT,CR_ACCT,a.DR_ACCT_TYPE,a.CR_ACCT_TYPE,"
				+ "a.DR_TRAN_CODE,a.CR_TRAN_CODE,a.DR_NARRATION,a.CR_NARRATION,a.AMOUNT,a.USER_ID,"
				+ "a.SUPER_ID,a.LEGACY_ID,a.POSTING_DATE,"
				+ "a.EFFECTIVE_DATE,a.PROCESS_STATUS,a.SUPER_ID,"
				+ "a.REJECT_REASON,a.TRAN_TYPE,a.TRAN_SENT_TIME,"
				+ "b.FULL_NAME,a.ASSET_ID,c.BRANCH_ID,c.DEPT_ID,"
				+ "c.CATEGORY_ID,c.REGISTRATION_NO,c.DESCRIPTION,c.ASSET_STATUS "
				+ "FROM am_asset_approval a JOIN AM_GB_USER b "
				+ "ON a.SUPER_ID = b.USER_ID  LEFT JOIN AM_ASSET_MAIN c "
				+ "ON (a.ASSET_ID=c.ASSET_ID)  "
				+ "WHERE a.BATCH_ID IS NOT NULL"
				+ query_;
				//+ " ,a.POSTING_DATE ";

		/* AND SUPER_ID = " + ; */
 
		String SELECT_QUERY = "SELECT DISTINCT a.BATCH_ID,a.TRANSACTION_ID,a.DR_ACCT,CR_ACCT,a.DR_ACCT_TYPE,a.CR_ACCT_TYPE,"
					+ "a.DR_TRAN_CODE,a.CR_TRAN_CODE,a.DR_NARRATION,a.CR_NARRATION,a.AMOUNT,a.USER_ID,"
					+ "a.SUPER_ID,a.LEGACY_ID,a.POSTING_DATE,"
					+ "a.EFFECTIVE_DATE,a.PROCESS_STATUS,a.SUPER_ID,"
					+ "a.REJECT_REASON,a.TRAN_TYPE,a.TRAN_SENT_TIME,"
					+ "b.FULL_NAME,a.ASSET_ID,a.BranchCode AS Branch_Id,c.DEPT_ID,"
					+ "c.CATEGORY_ID,c.REGISTRATION_NO,a.DESCRIPTION,c.ASSET_STATUS,a.asset_code,a.PROCESSING "
					+ "FROM am_asset_approval a JOIN AM_GB_USER b "
					+ "ON a.SUPER_ID = b.USER_ID  JOIN AM_ASSET c ON (a.ASSET_ID=c.ASSET_ID)  "
					+ "WHERE a.BATCH_ID IS NOT NULL AND (a.PROCESS_STATUS = 'P' OR a.PROCESS_STATUS = 'F' OR a.PROCESS_STATUS = 'A'  OR a.PROCESS_STATUS = 'U' "
					+ "OR a.PROCESS_STATUS = 'Q'  OR a.PROCESS_STATUS = 'C' OR a.PROCESS_STATUS = 'PR' OR a.PROCESS_STATUS = 'R') "+query_+"  and a.posting_date between getdate()-365 and getdate() "
					+ " UNION "
					+"SELECT DISTINCT a.BATCH_ID,a.TRANSACTION_ID,a.DR_ACCT,CR_ACCT,a.DR_ACCT_TYPE,a.CR_ACCT_TYPE,"
					+ "a.DR_TRAN_CODE,a.CR_TRAN_CODE,a.DR_NARRATION,a.CR_NARRATION,a.AMOUNT,a.USER_ID,"
					+ "a.SUPER_ID,a.LEGACY_ID,a.POSTING_DATE,"
					+ "a.EFFECTIVE_DATE,a.PROCESS_STATUS,a.SUPER_ID,"
					+ "a.REJECT_REASON,a.TRAN_TYPE,a.TRAN_SENT_TIME,"
					+ "b.FULL_NAME,a.ASSET_ID,a.BranchCode AS Branch_Id,c.DEPT_ID,"
					+ "c.CATEGORY_ID,c.REGISTRATION_NO,a.DESCRIPTION,c.ASSET_STATUS,a.asset_code,a.PROCESSING "
					+ "FROM am_asset_approval a JOIN AM_GB_USER b ON a.SUPER_ID = b.USER_ID JOIN AM_ASSET_UNCAPITALIZED c ON (a.ASSET_ID=c.ASSET_ID)  "
					+ "WHERE a.BATCH_ID IS NOT NULL AND (a.PROCESS_STATUS = 'P' OR a.PROCESS_STATUS = 'F' OR a.PROCESS_STATUS = 'A'  OR a.PROCESS_STATUS = 'U' "
					+ "OR a.PROCESS_STATUS = 'Q'  OR a.PROCESS_STATUS = 'C' OR a.PROCESS_STATUS = 'PR' OR a.PROCESS_STATUS = 'R' OR a.PROCESS_STATUS = 'RP') "+query_+"  and a.posting_date between getdate()-365 and getdate() "
					+ " UNION "
					+"SELECT DISTINCT a.BATCH_ID,a.TRANSACTION_ID,a.DR_ACCT,CR_ACCT,a.DR_ACCT_TYPE,a.CR_ACCT_TYPE,"
					+ "a.DR_TRAN_CODE,a.CR_TRAN_CODE,a.DR_NARRATION,a.CR_NARRATION,a.AMOUNT,a.USER_ID,"
					+ "a.SUPER_ID,a.LEGACY_ID,a.POSTING_DATE,"
					+ "a.EFFECTIVE_DATE,a.PROCESS_STATUS,a.SUPER_ID,"
					+ "a.REJECT_REASON,a.TRAN_TYPE,a.TRAN_SENT_TIME,"
					+ "b.FULL_NAME,a.ASSET_ID,a.BranchCode AS Branch_Id,c.DEPT_ID,"
					+ "c.CATEGORY_ID,c.REGISTRATION_NO,a.DESCRIPTION,c.ASSET_STATUS,a.asset_code,a.PROCESSING "
					+ "FROM am_asset_approval a JOIN AM_GB_USER b "
					+ "ON a.SUPER_ID = b.USER_ID  LEFT JOIN AM_ASSET c " 
					+ "ON (a.ASSET_ID=c.ASSET_ID)  "
					+ "WHERE a.BATCH_ID IS NOT NULL AND (a.PROCESS_STATUS = 'R' OR a.PROCESS_STATUS = 'RP' OR a.PROCESS_STATUS = 'A' OR a.PROCESS_STATUS = 'P') AND a.PROCESSING IS NULL "+query_+" and a.posting_date between getdate()-365 and getdate() and a.POSTING_DATE between getdate()-365 and getdate() "
					+ "AND a.tran_type NOT IN ('Uncapitalized Asset Disposal','Asset Creation Uncapitalized','Asset Creation','Uncapitalized Improvement')"
					+ " UNION "	 
					+"SELECT DISTINCT a.BATCH_ID,a.TRANSACTION_ID,a.DR_ACCT,CR_ACCT,a.DR_ACCT_TYPE,a.CR_ACCT_TYPE,"
					+ "a.DR_TRAN_CODE,a.CR_TRAN_CODE,a.DR_NARRATION,a.CR_NARRATION,a.AMOUNT,a.USER_ID,"
					+ "a.SUPER_ID,a.LEGACY_ID,a.POSTING_DATE,"
					+ "a.EFFECTIVE_DATE,a.PROCESS_STATUS,a.SUPER_ID,"
					+ "a.REJECT_REASON,a.TRAN_TYPE,a.TRAN_SENT_TIME,"
					+ "b.FULL_NAME,a.ASSET_ID,a.BranchCode AS Branch_Id,c.DEPT_ID,"
					+ "c.CATEGORY_ID,c.REGISTRATION_NO,a.DESCRIPTION,c.ASSET_STATUS,a.asset_code,a.PROCESSING "
					+ "FROM am_asset_approval a JOIN AM_GB_USER b "
					+ "ON a.SUPER_ID = b.USER_ID  LEFT JOIN AM_ASSET c " 
					+ "ON (a.ASSET_ID=c.ASSET_ID)  "
					+ "WHERE a.BATCH_ID IS NOT NULL AND (a.PROCESS_STATUS = 'R' OR a.PROCESS_STATUS = 'RP' OR a.PROCESS_STATUS = 'R') AND a.PROCESSING = 'Processing Rejection' "+query_+" and a.posting_date between getdate()-365 and getdate() and a.DATE_APPROVED between getdate()-365 and getdate() "
					+ " UNION "
					+"SELECT DISTINCT a.BATCH_ID,NULL AS TRANSACTION_ID,NULL AS DR_ACCT,NULL AS CR_ACCT,NULL AS DR_ACCT_TYPE,NULL AS CR_ACCT_TYPE,NULL AS DR_TRAN_CODE,NULL AS CR_TRAN_CODE,"
					+ "NULL AS DR_NARRATION,NULL AS CR_NARRATION,NULL AS AMOUNT,a.USER_ID,NULL AS SUPER_ID,NULL AS LEGACY_ID,(SELECT GETDATE()) AS POSTING_DATE,(SELECT GETDATE()) AS EFFECTIVE_DATE,a.PROCESS_STATUS,"
					+ "NULL AS SUPER_ID,'' AS REJECT_REASON,'Branch Asset Proof' AS TRAN_TYPE,NULL AS TRAN_SENT_TIME,'NOT YET SEND FOR APPROVAL' AS FULL_NAME,a.BATCH_ID AS ASSET_ID,a.Branch_Id,NULL AS DEPT_ID,NULL AS CATEGORY_ID,NULL AS REGISTRATION_NO,"
					+ "'Branch Asset Proof'AS DESCRIPTION,NULL AS ASSET_STATUS,NULL AS asset_code, '' AS PROCESSING FROM am_Asset_Proof_View a JOIN AM_GB_USER b ON a.USER_ID = b.USER_ID  LEFT JOIN AM_ASSET c "
					+ "ON (a.ASSET_ID=c.ASSET_ID)  WHERE a.BATCH_ID IS NOT NULL AND (a.PROCESS_STATUS IS NULL)  "+query_+" "
					+ "and a.posting_date between getdate()-365 and getdate() and a.PROOF_DATE between getdate()-365 and getdate()   and a.PROOF_DATE between getdate()-365 and getdate() "
					+ " UNION "
					+"SELECT DISTINCT a.BATCH_ID,NULL AS TRANSACTION_ID,NULL AS DR_ACCT,NULL AS CR_ACCT,NULL AS DR_ACCT_TYPE,NULL AS CR_ACCT_TYPE,NULL AS DR_TRAN_CODE,NULL AS CR_TRAN_CODE,"
					+ "NULL AS DR_NARRATION,NULL AS CR_NARRATION,NULL AS AMOUNT,a.USER_ID,NULL AS SUPER_ID,NULL AS LEGACY_ID,(SELECT GETDATE()) AS POSTING_DATE,(SELECT GETDATE()) AS EFFECTIVE_DATE,'A' AS PROCESS_STATUS,"
					+ "NULL AS SUPER_ID,'' AS REJECT_REASON,'Branch Asset Proof' AS TRAN_TYPE,NULL AS TRAN_SENT_TIME,b.FULL_NAME,a.BATCH_ID AS ASSET_ID,a.Branch_Id,NULL AS DEPT_ID,NULL AS CATEGORY_ID,NULL AS REGISTRATION_NO,"
					+ "'Branch Asset Proof'AS DESCRIPTION,NULL AS ASSET_STATUS,NULL AS asset_code,'' AS PROCESSING FROM am_Asset_Proof_View a JOIN AM_GB_USER b ON a.USER_ID = b.USER_ID  LEFT JOIN AM_ASSET c "
					+ "ON (a.ASSET_ID=c.ASSET_ID)  WHERE a.BATCH_ID IS NOT NULL AND (a.PROCESS_STATUS = 'APPROVED')  "+query_+" "
					+ "and a.posting_date between getdate()-365 and getdate() and a.PROOF_DATE between getdate()-365 and getdate()   and a.PROOF_DATE between getdate()-365 and getdate() "
					+ "ORDER BY POSTING_DATE DESC";
//		 String SELECT_QUERY = aprecords.getCodeName("select PREFIX from ACCOUNT_GLPREFIX_PARAM where THIRDPARTY = 'TRANSACTSTATUS' ");
        System.out.println("SELECT_QUERY >>>>>>>" + SELECT_QUERY); 
		ArrayList list = new ArrayList();   
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try { 

			con = getConnection("legendPlus");
			ps = con.prepareStatement(SELECT_QUERY);
//			ps.setString(1,query_);
//			ps.setString(2,query_);
//			ps.setString(3,query_);
//			ps.setString(4,query_);
//			ps.setString(5,query_);
//			ps.setString(6,query_);
			rs = ps.executeQuery();

			while (rs.next()) {

				String id = rs.getString("TRANSACTION_ID");
				// System.out.println(id);
				String debitAccount = rs.getString("DR_ACCT");
				String creditAccount = rs.getString("CR_ACCT");
				String drAcctType = rs.getString("DR_ACCT_TYPE");
				String crAcctType = rs.getString("CR_ACCT_TYPE");
				String drTranCode = rs.getString("DR_TRAN_CODE");
				String crTranCode = rs.getString("CR_TRAN_CODE");
				String creditNarration = rs.getString("CR_NARRATION");
				String debitNarration = rs.getString("DR_NARRATION");
				double amount = rs.getDouble("AMOUNT");
				userId = rs.getString("USER_ID");
				String superId = rs.getString("SUPER_ID");
				String legacyCode = rs.getString("LEGACY_ID");
				String code = "";// rs.getString("TRAN_CODE");
				String postingDate = formatDate(rs.getDate("POSTING_DATE"));
				String effectiveDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
				String status = rs.getString("PROCESS_STATUS");
				String supervisor = rs.getString("SUPER_ID");
				String rejectReason = rs.getString("REJECT_REASON");
				String batchId = rs.getString("BATCH_ID");
				String fullName = rs.getString("FULL_NAME");
				String assetId = rs.getString("ASSET_ID");
				int branchId = rs.getInt("BRANCH_ID");
				int deptId = rs.getInt("DEPT_ID");
				int categoryId = rs.getInt("CATEGORY_ID");
				String regNo = rs.getString("REGISTRATION_NO");
				String desc = rs.getString("DESCRIPTION");
				// double cost = rs.getDouble("COST_PRICE");
				String processing = rs.getString("PROCESSING");
				String assetStatus = rs.getString("ASSET_STATUS");
				String tranType = rs.getString("TRAN_TYPE");
				String sentTime = rs.getString("TRAN_SENT_TIME");
                int assetCode = rs.getInt("asset_code");
				Transaction tran = new Transaction(id, debitAccount,
						creditAccount, creditNarration, debitNarration, amount,
						userId, superId, legacyCode, code, postingDate,
						effectiveDate, status, supervisor, rejectReason,
						batchId, fullName, assetId, drAcctType, crAcctType,
						drTranCode, crTranCode, branchId, deptId, categoryId,
						regNo, desc, assetStatus, tranType, sentTime);
                        tran.setAssetCode(assetCode);
                        tran.setBranchId(branchId);
                        tran.setProcessing(processing);
				list.add(tran);
 
			}

		} catch (Exception e) {
			String warning = "WARNING:Error Fetching Transaction for Approval"
					+ " ->" + e.getMessage();
			System.out.println(warning);
		} finally {
			closeConnection(con, ps, rs);
		}
		return list;
	}

	// get transaction status for user
	public ArrayList findTransactionStatusListforApproval(String query_) {

		String SELECT_QUERY_OLD = "SELECT DISTINCT a.BATCH_ID,a.TRANSACTION_ID,a.DR_ACCT,CR_ACCT,a.DR_ACCT_TYPE,a.CR_ACCT_TYPE,"
				+ "a.DR_TRAN_CODE,a.CR_TRAN_CODE,a.DR_NARRATION,a.CR_NARRATION,a.AMOUNT,a.USER_ID,"
				+ "a.SUPER_ID,a.LEGACY_ID,a.POSTING_DATE,"
				+ "a.EFFECTIVE_DATE,a.PROCESS_STATUS,a.SUPER_ID,"
				+ "a.REJECT_REASON,a.TRAN_TYPE,a.TRAN_SENT_TIME,"
				+ "b.FULL_NAME,a.ASSET_ID,c.BRANCH_ID,c.DEPT_ID,"
				+ "c.CATEGORY_ID,c.REGISTRATION_NO,c.DESCRIPTION,c.ASSET_STATUS "
				+ "FROM am_asset_approval a JOIN AM_GB_USER b "
				+ "ON a.SUPER_ID = b.USER_ID  LEFT JOIN AM_ASSET_MAIN c "
				+ "ON (a.ASSET_ID=c.ASSET_ID)  "
				+ "WHERE a.BATCH_ID IS NOT NULL"
				+ query_;
				//+ " ,a.POSTING_DATE ";

		/* AND SUPER_ID = " + ; */

		String SELECT_QUERY = "SELECT DISTINCT a.BATCH_ID,a.TRANSACTION_ID,a.DR_ACCT,CR_ACCT,a.DR_ACCT_TYPE,a.CR_ACCT_TYPE,"
					+ "a.DR_TRAN_CODE,a.CR_TRAN_CODE,a.DR_NARRATION,a.CR_NARRATION,a.AMOUNT,a.USER_ID,"
					+ "a.SUPER_ID,a.LEGACY_ID,a.POSTING_DATE,"
					+ "a.EFFECTIVE_DATE,a.PROCESS_STATUS,a.SUPER_ID,"
					+ "a.REJECT_REASON,a.TRAN_TYPE,a.TRAN_SENT_TIME,"
					+ "b.FULL_NAME,a.ASSET_ID,c.BRANCH_ID,c.DEPT_ID,"
					+ "c.CATEGORY_ID,c.REGISTRATION_NO,a.DESCRIPTION,c.ASSET_STATUS,a.asset_code "
					+ "FROM am_asset_approval a JOIN AM_GB_USER b "
					+ "ON a.SUPER_ID = b.USER_ID  LEFT JOIN AM_ASSET c "
					+ "ON (a.ASSET_ID=c.ASSET_ID)  "
					+ "WHERE a.BATCH_ID IS NOT NULL "+query_+" "
					+ " UNION "
					+ "SELECT DISTINCT a.BATCH_ID,NULL AS TRANSACTION_ID,NULL AS DR_ACCT,NULL AS CR_ACCT,NULL AS DR_ACCT_TYPE,NULL AS CR_ACCT_TYPE,NULL AS DR_TRAN_CODE,NULL AS CR_TRAN_CODE,NULL AS DR_NARRATION,"
					+ "NULL AS CR_NARRATION,0 AS AMOUNT,'0' AS USER_ID,'0' AS SUPER_ID,NULL AS LEGACY_ID,a.PROOF_DATE,a.PROOF_DATE,'P' AS PROCESS_STATUS,'0' AS SUPER_ID,'' AS REJECT_REASON,"
					+ "'Asset Proof Creation' AS TRAN_TYPE,'00:00:00' AS TRAN_SENT_TIME,'NOT YET SEND FOR APPROVAL' AS FULL_NAME,a.BATCH_ID AS ASSET_ID,c.BRANCH_ID,c.DEPT_ID,c.CATEGORY_ID,NULL AS REGISTRATION_NO,'Asset Proof Creation' AS DESCRIPTION,c.ASSET_STATUS,"
					+ "'0' AS asset_code FROM am_Asset_Proof a LEFT JOIN AM_ASSET c ON (a.ASSET_ID=c.ASSET_ID) "
					+ "WHERE a.BATCH_ID IS NOT NULL AND PROCESS_STATUS = 'APPROVED' ORDER BY POSTING_DATE DESC ";
 
//                System.out.println("SELECT_QUERY >>>>>>>" + SELECT_QUERY);
		ArrayList list = new ArrayList();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			con = getConnection("legendPlus");
			ps = con.prepareStatement(SELECT_QUERY);
			// ps.setInt(1,Integer.parseInt(superid));
			rs = ps.executeQuery();

			while (rs.next()) {

				String id = rs.getString("TRANSACTION_ID");
				// System.out.println(id);
				String debitAccount = rs.getString("DR_ACCT");
				String creditAccount = rs.getString("CR_ACCT");
				String drAcctType = rs.getString("DR_ACCT_TYPE");
				String crAcctType = rs.getString("CR_ACCT_TYPE");
				String drTranCode = rs.getString("DR_TRAN_CODE");
				String crTranCode = rs.getString("CR_TRAN_CODE");
				String creditNarration = rs.getString("CR_NARRATION");
				String debitNarration = rs.getString("DR_NARRATION");
				double amount = rs.getDouble("AMOUNT");
				String userId = rs.getString("USER_ID");
				String superId = rs.getString("SUPER_ID");
				String legacyCode = rs.getString("LEGACY_ID");
				String code = "";// rs.getString("TRAN_CODE");
				String postingDate = formatDate(rs.getDate("POSTING_DATE"));
				String effectiveDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
				String status = rs.getString("PROCESS_STATUS");
				String supervisor = rs.getString("SUPER_ID");
				String rejectReason = rs.getString("REJECT_REASON");
				String batchId = rs.getString("BATCH_ID");
				String fullName = rs.getString("FULL_NAME");
				String assetId = rs.getString("ASSET_ID");
				int branchId = rs.getInt("BRANCH_ID");
				int deptId = rs.getInt("DEPT_ID");
				int categoryId = rs.getInt("CATEGORY_ID");
				String regNo = rs.getString("REGISTRATION_NO");
				String desc = rs.getString("DESCRIPTION");
				// double cost = rs.getDouble("COST_PRICE");
				// String assetUser = rs.getString("ASSET_USER");
				String assetStatus = rs.getString("ASSET_STATUS");
				String tranType = rs.getString("TRAN_TYPE");
				String sentTime = rs.getString("TRAN_SENT_TIME");
                int assetCode = rs.getInt("asset_code");
				Transaction tran = new Transaction(id, debitAccount,
						creditAccount, creditNarration, debitNarration, amount,
						userId, superId, legacyCode, code, postingDate,
						effectiveDate, status, supervisor, rejectReason,
						batchId, fullName, assetId, drAcctType, crAcctType,
						drTranCode, crTranCode, branchId, deptId, categoryId,
						regNo, desc, assetStatus, tranType, sentTime);
                                                tran.setAssetCode(assetCode);
				list.add(tran);

			}
			closeConnection(con, ps, rs);
		} catch (Exception e) {
			String warning = "WARNING:Error Fetching Transaction for Approval"
					+ " ->" + e.getMessage();
			System.out.println(warning);
		} finally {
			closeConnection(con, ps, rs);
		}
		return list;
	}
		
	// get transaction status for user
	public ArrayList findTransactionStatusListInventory(String query_) {

		String SELECT_QUERY_OLD = "SELECT DISTINCT a.BATCH_ID,a.TRANSACTION_ID,a.DR_ACCT,CR_ACCT,a.DR_ACCT_TYPE,a.CR_ACCT_TYPE,"
				+ "a.DR_TRAN_CODE,a.CR_TRAN_CODE,a.DR_NARRATION,a.CR_NARRATION,a.AMOUNT,a.USER_ID,"
				+ "a.SUPER_ID,a.LEGACY_ID,a.POSTING_DATE,d.quantity_request,"
				+ "a.EFFECTIVE_DATE,a.PROCESS_STATUS,a.SUPER_ID,"
				+ "a.REJECT_REASON,a.TRAN_TYPE,a.TRAN_SENT_TIME,"
				+ "b.FULL_NAME,a.ASSET_ID,c.BRANCH_ID,c.DEPT_ID,"
				+ "c.CATEGORY_ID,c.REGISTRATION_NO,c.DESCRIPTION,c.ASSET_STATUS "
				+ "FROM am_asset_approval a JOIN AM_GB_USER b "
				+ "ON a.SUPER_ID = b.USER_ID  LEFT JOIN AM_ASSET_MAIN c "
				+ "ON (a.ASSET_ID=c.ASSET_ID) LEFT JOIN ST_DISTRIBUTION_ITEM d ON a.asset_id = d.reqnId  "
				+ "WHERE a.BATCH_ID IS NOT NULL"
				+ query_;
				//+ " ,a.POSTING_DATE ";

		/* AND SUPER_ID = " + ; */

		String SELECT_QUERY = "SELECT DISTINCT a.BATCH_ID,a.TRANSACTION_ID,a.DR_ACCT,CR_ACCT,a.DR_ACCT_TYPE,a.CR_ACCT_TYPE,"
					+ "a.DR_TRAN_CODE,a.CR_TRAN_CODE,a.DR_NARRATION,a.CR_NARRATION,a.AMOUNT,a.USER_ID,"
					+ "a.SUPER_ID,a.LEGACY_ID,a.POSTING_DATE,d.quantity_request,"
					+ "a.EFFECTIVE_DATE,a.PROCESS_STATUS,a.SUPER_ID,e.approval_status,"
					+ "a.REJECT_REASON,a.TRAN_TYPE,a.TRAN_SENT_TIME,"
					+ "b.FULL_NAME,a.ASSET_ID,c.BRANCH_ID,c.DEPT_ID,"
					+ "c.CATEGORY_ID,c.REGISTRATION_NO,a.DESCRIPTION,c.ASSET_STATUS,a.asset_code "
					+ "FROM am_asset_approval a JOIN AM_GB_USER b "
					+ "ON a.SUPER_ID = b.USER_ID  LEFT JOIN AM_ASSET_MAIN c "
					+ "ON (a.ASSET_ID=c.ASSET_ID) LEFT JOIN ST_DISTRIBUTION_ITEM d ON a.asset_id = d.reqnId "
					+ "LEFT JOIN am_assetTransfer e ON a.asset_id = e.asset_id WHERE a.BATCH_ID IS NOT NULL"
					+ query_;
 
 //               System.out.println("SELECT_QUERY >>>>>>>" + SELECT_QUERY);
		ArrayList list = new ArrayList();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			con = getConnection("legendPlus");
			ps = con.prepareStatement(SELECT_QUERY);
			// ps.setInt(1,Integer.parseInt(superid));
			rs = ps.executeQuery();

			while (rs.next()) {

				String id = rs.getString("TRANSACTION_ID");
				// System.out.println(id);
				String debitAccount = rs.getString("DR_ACCT");
				String creditAccount = rs.getString("CR_ACCT");
				String drAcctType = rs.getString("DR_ACCT_TYPE");
				String crAcctType = rs.getString("CR_ACCT_TYPE");
				String drTranCode = rs.getString("DR_TRAN_CODE");
				String crTranCode = rs.getString("CR_TRAN_CODE");
				String creditNarration = rs.getString("CR_NARRATION");
				String debitNarration = rs.getString("DR_NARRATION");
				double amount = rs.getDouble("AMOUNT");
				String userId = rs.getString("USER_ID");
				String superId = rs.getString("SUPER_ID");
				String legacyCode = rs.getString("LEGACY_ID");
				String code = "";// rs.getString("TRAN_CODE");
				String postingDate = formatDate(rs.getDate("POSTING_DATE"));
				String effectiveDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
				String quantity = rs.getString("quantity_request");
				String status = rs.getString("PROCESS_STATUS");
				String supervisor = rs.getString("SUPER_ID");
				String rejectReason = rs.getString("REJECT_REASON");
				String batchId = rs.getString("BATCH_ID");
				String fullName = rs.getString("FULL_NAME");
				String assetId = rs.getString("ASSET_ID");
				int branchId = rs.getInt("BRANCH_ID");
				int deptId = rs.getInt("DEPT_ID");
				int categoryId = rs.getInt("CATEGORY_ID");
				String regNo = rs.getString("REGISTRATION_NO");
				String desc = rs.getString("DESCRIPTION");
				// double cost = rs.getDouble("COST_PRICE");
				// String assetUser = rs.getString("ASSET_USER");
				String assetStatus = rs.getString("ASSET_STATUS");
				String tranType = rs.getString("TRAN_TYPE");
				String sentTime = rs.getString("TRAN_SENT_TIME");
                                int assetCode = rs.getInt("asset_code");
 //               System.out.println("<<<<<<<<quantity: "+quantity+"    Asset Id: "+assetId);
                if(quantity!=null){status = "I";}  // I stands for Issuance;
//                System.out.println("<<<<<<<<status: "+status); 
				Transaction tran = new Transaction(id, debitAccount,
						creditAccount, creditNarration, debitNarration, amount,
						userId, superId, legacyCode, code, postingDate,
						effectiveDate, status, supervisor, rejectReason,
						batchId, fullName, assetId, drAcctType, crAcctType,
						drTranCode, crTranCode, branchId, deptId, categoryId,
						regNo, desc, assetStatus, tranType, sentTime);
                                                tran.setAssetCode(assetCode);
				list.add(tran);

			}

		} catch (Exception e) {
			String warning = "WARNING:Error Fetching Transaction for Approval"
					+ " ->" + e.getMessage();
			System.out.println(warning);
		} finally {
			closeConnection(con, ps, rs);
		}
		return list;
	}
	
	public ArrayList findTransactionStatusLists(String query_) {

		String SELECT_QUERY = "SELECT DISTINCT a.BATCH_ID,a.POSTING_DATE,"
				+ "a.EFFECTIVE_DATE,a.PROCESS_STATUS,a.SUPER_ID,"
				+ "a.TRAN_TYPE,a.USER_ID,"
				+ "b.FULL_NAME,a.ASSET_ID,c.BRANCH_ID,c.DEPT_ID,"
				+ "c.CATEGORY_ID,c.REGISTRATION_NO,c.DESCRIPTION,c.ASSET_STATUS "
				+ "FROM am_asset_approval a INNER JOIN AM_GB_USER b "
				+ "ON a.SUPER_ID = b.USER_ID  INNER JOIN AM_ASSET_MAIN c "
				+ "ON (a.ASSET_ID=c.ASSET_ID)  "
				+ "WHERE a.BATCH_ID IS NOT NULL"
				+ query_
				+ " ORDER BY a.POSTING_DATE DESC";

		/* AND SUPER_ID = " + ; */
		ArrayList list = new ArrayList();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			con = getConnection("legendPlus");
			ps = con.prepareStatement(SELECT_QUERY);
			// ps.setInt(1,Integer.parseInt(superid));
			rs = ps.executeQuery();

			while (rs.next()) {

				String id = "";//rs.getString("TRANSACTION_ID");
				// System.out.println(id);
				String debitAccount = "";
				String creditAccount = "";
				String drAcctType = "";
				String crAcctType = "";
				String drTranCode = "";
				String crTranCode = "";
				String creditNarration = "";
				String debitNarration = "";
				double amount = 0.00d;
				String userId = rs.getString("USER_ID");
				String superId = rs.getString("SUPER_ID");
				String legacyCode = "";
				String code = "";// rs.getString("TRAN_CODE");
				String postingDate = formatDate(rs.getDate("POSTING_DATE"));
				String effectiveDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
				String status = rs.getString("PROCESS_STATUS");
				String supervisor = rs.getString("SUPER_ID");
				String rejectReason = "";
				String batchId = rs.getString("BATCH_ID");
				String fullName = rs.getString("FULL_NAME");
				String assetId = rs.getString("ASSET_ID");
				int branchId = rs.getInt("BRANCH_ID");
				int deptId = rs.getInt("DEPT_ID");
				int categoryId = rs.getInt("CATEGORY_ID");
				String regNo = rs.getString("REGISTRATION_NO");
				String desc = rs.getString("DESCRIPTION");
				// double cost = rs.getDouble("COST_PRICE");
				// String assetUser = rs.getString("ASSET_USER");
				String assetStatus = rs.getString("ASSET_STATUS");
				String tranType = rs.getString("TRAN_TYPE");
				String sentTime = "";

				Transaction tran = new Transaction(id, debitAccount,
						creditAccount, creditNarration, debitNarration, amount,
						userId, superId, legacyCode, code, postingDate,
						effectiveDate, status, supervisor, rejectReason,
						batchId, fullName, assetId, drAcctType, crAcctType,
						drTranCode, crTranCode, branchId, deptId, categoryId,
						regNo, desc, assetStatus, tranType, sentTime);

				list.add(tran);

			}

		} catch (Exception e) {
			String warning = "WARNING:Error Fetching Transaction for Approval"
					+ " ->" + e.getMessage();
			System.out.println(warning);
		} finally {
			closeConnection(con, ps, rs);
		}
		return list;
	}
	public void deleteTransaction(String batchId, String tranType) {
		String query = "DELETE FROM am_asset_approval WHERE ASSET_ID = ? ";

		String updateTranType = "";

		if (tranType.equalsIgnoreCase("DISPOSAL")) {
			updateTranType = "UPDATE AM_ASSETDISPOSAL SET RAISE_ENTRY = 'N' WHERE ASSET_ID ='"
					+ batchId + "'";
		} else if (tranType.equalsIgnoreCase("TRANSFER")) {
			updateTranType = "UPDATE AM_ASSETTRANSFER SET RAISE_ENTRY = 'N' WHERE ASSET_ID ='"
					+ batchId + "'";
		} else if (tranType.equalsIgnoreCase("REVALUE")) {
			updateTranType = "UPDATE AM_ASSETREVALUE SET RAISE_ENTRY = 'N' WHERE ASSET_ID ='"
					+ batchId + "'";
		} else {
		}

		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(query);
			ps.setString(1, batchId);
			ps.execute();
			ps = con.prepareStatement(updateTranType);
			int x = ps.executeUpdate();
		} catch (Exception e) {
			String warning = "WARNING:Error Deleting Transaction" + " ->"
					+ e.getMessage();
			System.out.println(warning);
		} finally {
			closeConnection(con, ps);
		}
	}

	public void deleteTransaction(String batchId, String assetId,
			String tranType) {
		String query = "DELETE FROM am_asset_approval WHERE BATCH_ID = ? ";

		String updateTranType = "";

		if (tranType.equalsIgnoreCase("DISPOSAL")) {
			updateTranType = "UPDATE AM_ASSETDISPOSAL SET RAISE_ENTRY = 'N' WHERE ASSET_ID ='"
					+ assetId + "'";
		} else if (tranType.equalsIgnoreCase("TRANSFER")) {
			updateTranType = "UPDATE AM_ASSETTRANSFER SET RAISE_ENTRY = 'N' WHERE ASSET_ID ='"
					+ assetId + "'";
		} else if (tranType.equalsIgnoreCase("REVALUE")) {
			updateTranType = "UPDATE AM_ASSETREVALUE SET RAISE_ENTRY = 'N' WHERE ASSET_ID ='"
					+ assetId + "'";
		} else if (tranType.equalsIgnoreCase("GROUP ACQUISITION")) {
			updateTranType = "UPDATE AM_GROUP_ASSET_MAIN SET RAISE_ENTRY = 'N' WHERE GROUP_ID ='"
					+ assetId + "'";
		} else if (tranType.equalsIgnoreCase("ACQUISITION")) {
			updateTranType = "UPDATE AM_ASSET SET RAISE_ENTRY = 'N' WHERE ASSET_ID ='"
					+ assetId + "'";
		} else if (tranType.equalsIgnoreCase("RECLASSIFICATION")) {
			updateTranType = "UPDATE AM_ASSETRECLASSIFICATION SET RAISE_ENTRY = 'N' WHERE ASSET_ID ='"
					+ assetId + "'";
		} else if (tranType.equalsIgnoreCase("DEPRECIATION ADJUSTMENT")) {
			updateTranType = "UPDATE AM_ASSET_DEP_ADJUSTMENT SET RAISE_ENTRY = 'N' WHERE ASSET_ID ='"
					+ assetId + "'";
		} else if (tranType.equalsIgnoreCase("ASSET PAYMENT")) {
			updateTranType = "UPDATE AM_ASSET_PAYMENT SET RAISE_ENTRY = 'N' WHERE PAY_ID ='"
					+ assetId + "'";
		} else if (tranType.equalsIgnoreCase("GROUP PAYMENT")) {
			updateTranType = "UPDATE AM_ASSET_PAYMENT SET RAISE_ENTRY = 'N' WHERE PAY_ID ='"
					+ assetId + "'";
		} else {

		}

		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(query);
			ps.setString(1, batchId);
			ps.execute();
			ps = con.prepareStatement(updateTranType);
			int x = ps.executeUpdate();
		} catch (Exception e) {
			String warning = "WARNING:Error Deleting Transaction" + " ->"
					+ e.getMessage();
			System.out.println(warning);
		} finally {
			closeConnection(con, ps);
		}
	}

	public void deleteTransaction(String[] batchId, String[] tranType) {
		if (batchId != null) {
			for (int x = 0; x < batchId.length; x++) {
				deleteTransaction(batchId[x], tranType[x]);
			}
		}

	}

	public void approveTransaction(String batchId, String command,
			String rReason) {
		String UPDATE_QUERY = "UPDATE am_asset_approval SET PROCESS_STATUS = ?, "
				+ "REJECT_REASON = ? WHERE ASSET_ID = ?";
		String status = "P";
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);

			ps.setString(1, command);
			ps.setString(2, rReason);
			ps.setString(3, batchId);

			ps.execute();

		} catch (Exception e) {
			String warning = "WARNING:Error Approving Transaction" + " ->"
					+ e.getMessage();
			System.out.println(warning);
		} finally {
			closeConnection(con, ps);
		}

	}

	public void approveTransactions(String batchId, String command,
			String rReason) {
		String UPDATE_QUERY = "UPDATE am_asset_approval SET PROCESS_STATUS = ?, "
				+ "REJECT_REASON = ? WHERE BATCH_ID = ?";
		//String status = "P";
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);

			ps.setString(1, command);
			ps.setString(2, rReason);
			ps.setString(3, batchId);

			ps.execute();

		} catch (Exception e) {
			String warning = "WARNING:Error Approving Transaction" + " ->"
					+ e.getMessage();
			System.out.println(warning);
		} finally {
			closeConnection(con, ps);
		}

	}
	public void reSubmit(String batchId, String command,
			int rReason) {
		String UPDATE_QUERY = "UPDATE am_asset_approval SET PROCESS_STATUS = ?, "
				+ " SUPER_ID = ? WHERE BATCH_ID = ?";
		//String status = "P";
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);

			ps.setString(1, command);
			ps.setInt(2, rReason);
			ps.setString(3, batchId);

			ps.execute();

		} catch (Exception e) {
			String warning = "WARNING:Error Approving Transaction" + " ->"
					+ e.getMessage();
			System.out.println(warning);
		} finally {
			closeConnection(con, ps);
		}

	}

	public void approveTransaction(String[] batchId, String command,
			String rReason) {
		if (batchId != null) {
			for (int x = 0; x < batchId.length; x++) {
				approveTransaction(batchId[x], command, rReason);
			}
		}

	}

	public ArrayList findTransactionForAuthorization(String query_) {
       // System.out.println("in method and the query is ............." +query_);
		String SELECT_QUERY = "SELECT DISTINCT a.BATCH_ID,a.TRANSACTION_ID,a.DR_ACCT,CR_ACCT,a.DR_ACCT_TYPE,a.CR_ACCT_TYPE,"
				+ "a.DR_TRAN_CODE,a.CR_TRAN_CODE,a.DR_NARRATION,a.CR_NARRATION,a.AMOUNT,a.USER_ID,"
				+ "a.SUPER_ID,a.LEGACY_ID,a.POSTING_DATE,"
				+ "a.EFFECTIVE_DATE,a.PROCESS_STATUS,a.SUPERVISOR,"
				+ "a.REJECT_REASON,a.TRAN_TYPE,a.TRAN_SENT_TIME,"
				+ "b.FULL_NAME,a.ASSET_ID,c.BRANCH_ID,c.DEPT_ID,"
				+ "c.CATEGORY_ID,c.REGISTRATION_NO,c.DESCRIPTION,c.ASSET_STATUS "
				+ "FROM am_asset_approval a JOIN AM_GB_USER b "
				+ "ON a.USER_ID = b.USER_ID  LEFT JOIN AM_ASSET_MAIN c "
				+ "ON (a.ASSET_ID=c.ASSET_ID)  "
				+ "WHERE a.BATCH_ID IS NOT NULL"
				+ query_
				+ " ORDER BY a.POSTING_DATE DESC";

		/* AND SUPER_ID = " + ; */
		ArrayList list = new ArrayList();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			con = getConnection("legendPlus");
			ps = con.prepareStatement(SELECT_QUERY);
			// ps.setInt(1,Integer.parseInt(superid));
			rs = ps.executeQuery();

			while (rs.next()) {

				String id = rs.getString("TRANSACTION_ID");
				// System.out.println(id);
				String debitAccount = rs.getString("DR_ACCT");
				String creditAccount = rs.getString("CR_ACCT");
				String drAcctType = rs.getString("DR_ACCT_TYPE");
				String crAcctType = rs.getString("CR_ACCT_TYPE");
				String drTranCode = rs.getString("DR_TRAN_CODE");
				String crTranCode = rs.getString("CR_TRAN_CODE");
				String creditNarration = rs.getString("CR_NARRATION");
				String debitNarration = rs.getString("DR_NARRATION");
				double amount = rs.getDouble("AMOUNT");
				String userId = rs.getString("USER_ID");
				String superId = rs.getString("SUPER_ID");
				String legacyCode = rs.getString("LEGACY_ID");
				String code = "";// rs.getString("TRAN_CODE");
				String postingDate = formatDate(rs.getDate("POSTING_DATE"));
				String effectiveDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
				String status = rs.getString("PROCESS_STATUS");
				String supervisor = rs.getString("SUPERVISOR");
				String rejectReason = rs.getString("REJECT_REASON");
				String batchId = rs.getString("BATCH_ID");
				String fullName = rs.getString("FULL_NAME");
				String assetId = rs.getString("ASSET_ID");
				int branchId = rs.getInt("BRANCH_ID");
				int deptId = rs.getInt("DEPT_ID");
				int categoryId = rs.getInt("CATEGORY_ID");
				String regNo = rs.getString("REGISTRATION_NO");
				String desc = rs.getString("DESCRIPTION");
				// double cost = rs.getDouble("COST_PRICE");
				// String assetUser = rs.getString("ASSET_USER");
				String assetStatus = rs.getString("ASSET_STATUS");
				String tranType = rs.getString("TRAN_TYPE");
				String sentTime = rs.getString("TRAN_SENT_TIME");

				Transaction tran = new Transaction(id, debitAccount,
						creditAccount, creditNarration, debitNarration, amount,
						userId, superId, legacyCode, code, postingDate,
						effectiveDate, status, supervisor, rejectReason,
						batchId, fullName, assetId, drAcctType, crAcctType,
						drTranCode, crTranCode, branchId, deptId, categoryId,
						regNo, desc, assetStatus, tranType, sentTime);

				list.add(tran);

			}

		} catch (Exception e) {
			String warning = "WARNING:Error Fetching Transaction for Approval"
					+ " ->" + e.getMessage();
			System.out.println(warning);
		} finally {
			closeConnection(con, ps, rs);
		}
//		      System.out.println("the size of the arrayy list is>>>>" +list.size());
        return list;
	}

	public ArrayList findTransactionForAuthorizations(String query_) {

		String SELECT_QUERY = "SELECT DISTINCT a.BATCH_ID,"
				+ "a.POSTING_DATE,"
				+ "a.EFFECTIVE_DATE,a.TRAN_TYPE,"
				+ "b.FULL_NAME "
				+ "FROM am_asset_approval a JOIN AM_GB_USER b "
				+ "ON a.USER_ID = b.USER_ID "
				+ "WHERE a.BATCH_ID IS NOT NULL "
				+ query_
				+ " ORDER BY a.POSTING_DATE DESC";

		/* AND SUPER_ID = " + ; */
		ArrayList list = new ArrayList();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			con = getConnection("legendPlus");
			ps = con.prepareStatement(SELECT_QUERY);
			// ps.setInt(1,Integer.parseInt(superid));
			rs = ps.executeQuery();

			while (rs.next()) {

				String id = "";//rs.getString("TRANSACTION_ID");
				// System.out.println(id);
				String debitAccount = "";
				String creditAccount = "";
				String drAcctType = "";
				String crAcctType = "";
				String drTranCode = "";
				String crTranCode = "";
				String creditNarration = "";
				String debitNarration = "";
				double amount = 0.00;
				String userId = "";
				String superId = "";
				String legacyCode = "";
				String code = "";// rs.getString("TRAN_CODE");
				String postingDate = formatDate(rs.getDate("POSTING_DATE"));
				String effectiveDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
				String status = "";
				String supervisor = "";
				String rejectReason = "";
				String batchId = rs.getString("BATCH_ID");
				String fullName = rs.getString("FULL_NAME");
				String assetId = "";
				int branchId = 0;
				int deptId = 0;
				int categoryId = 0;
				String regNo = "";
				String desc = "";
				// double cost = rs.getDouble("COST_PRICE");
				// String assetUser = rs.getString("ASSET_USER");
				String assetStatus = "";
				String tranType = rs.getString("TRAN_TYPE");
				String sentTime = "";

				Transaction tran = new Transaction(id, debitAccount,
						creditAccount, creditNarration, debitNarration, amount,
						userId, superId, legacyCode, code, postingDate,
						effectiveDate, status, supervisor, rejectReason,
						batchId, fullName, assetId, drAcctType, crAcctType,
						drTranCode, crTranCode, branchId, deptId, categoryId,
						regNo, desc, assetStatus, tranType, sentTime);

				list.add(tran);

			}

		} catch (Exception e) {
			String warning = "WARNING:Error Fetching Transaction for Approval"
					+ " ->" + e.getMessage();
			System.out.println(warning);
		} finally {
			closeConnection(con, ps, rs);
		}
		return list;
	}

	public void updateTransactionStatus(String batchId, String status) {
		String UPDATE_QUERY = "UPDATE am_asset_approval SET PROCESS_STATUS = ? "
				+ "WHERE ASSET_ID = ?";
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(UPDATE_QUERY);
			ps.setString(1, status);
			ps.setString(2, batchId);
			ps.execute();

		} catch (Exception e) {
			String warning = "WARNING:Error updating time out Transaction"
					+ " ->" + e.getMessage();
			System.out.println(warning);
		} finally {
			closeConnection(con, ps);
		}

	}

	public boolean isWaitingTimeUp(int waitTime_, String sentTime_) {
		String sYear, sMonth, sDate, sHour, sMin, sSec, sMilli;
		Integer iYear, iMonth, iDate, iHour, iMin, iSec, iMilli;
		int iAMPM, intYear, intMonth, intDate, intHour, intMin, intSec, intMilli;
		// boolean result = true;
		int waitTime = waitTime_; // get this from company table
		String sentTime = sentTime_.substring(0, 19);
		boolean result = false;

		java.util.Calendar calendar = java.util.Calendar.getInstance();

		iYear = new Integer(calendar.get(calendar.YEAR));
		intMonth = calendar.get(calendar.MONTH) + 1;

		iMonth = new Integer(intMonth); // Return value is zero based so add 1
		// to get correct value

		intDate = calendar.get(calendar.DATE);

		iDate = new Integer(intDate);
		iHour = new Integer(calendar.get(calendar.HOUR));
		iMin = new Integer(calendar.get(calendar.MINUTE));

//		System.out.println("before " + iYear.toString() + "-"
//				+ iMonth.toString() + "-" + iDate.toString() + "-"
//				+ iHour.toString() + "-" + iMin.toString());
		String currentTime = iYear.toString() + iMonth.toString()
				+ iDate.toString() + iHour.toString() + iMin.toString();

		intYear = new Integer(sentTime.substring(0, 4)).intValue();
		intMonth = new Integer(sentTime.substring(5, 7)).intValue();
		intDate = new Integer(sentTime.substring(8, 10)).intValue();
		intHour = new Integer(sentTime.substring(11, 13)).intValue();
		intMin = new Integer(sentTime.substring(14, 16)).intValue();
		intSec = new Integer(sentTime.substring(17, 19)).intValue();

		// Calendar calendar = Calendar.getInstance();
		calendar.set(intYear, intMonth - 1, intDate, intHour, intMin);

		calendar.add(calendar.MINUTE, waitTime);

		iYear = new Integer(calendar.get(calendar.YEAR));
		intMonth = calendar.get(calendar.MONTH) + 1;

		iMonth = new Integer(intMonth); // Return value is zero based so add 1
		// to get correct value

		intDate = calendar.get(calendar.DATE);

		iDate = new Integer(intDate);
		iHour = new Integer(calendar.get(calendar.HOUR));
		iMin = new Integer(calendar.get(calendar.MINUTE));

		String sentTimeUp = iYear.toString() + iMonth.toString()
				+ iDate.toString() + iHour.toString() + iMin.toString();
//		System.out.println("after " + iYear.toString() + "-"
//				+ iMonth.toString() + "-" + iDate.toString() + "-"
//				+ iHour.toString() + "-" + iMin.toString());
		if (Double.parseDouble(currentTime) >= Double.parseDouble(sentTimeUp)) {
			result = true;
		}

		return result;

	}

	public void updateTransactionStatus(ArrayList tranids, String status) {

		CompanyHandler raiseMan = new CompanyHandler();
		int waitTime = (int) (raiseMan.getCompany().getTransWaitTime());
		if (tranids != null) {
			for (int x = 0; x < tranids.size(); x++) {
				String assetId = ((Transaction) tranids.get(x)).getAssetId();
				String tranType = ((Transaction) tranids.get(x)).getTranType();
				String sentTime = ((Transaction) tranids.get(x)).getSentTime();
				// String updateQuery = getOperationQuery(tranType,assetId);
				if (isWaitingTimeUp(waitTime, sentTime))
                {
					updateTransactionStatus(((Transaction) tranids.get(x)).getAssetId(), status);
					setOperationRaiseEntryStatus(assetId, tranType);
					// raiseMan.setRaiseEntryStatus(updateQuery);
				}
			}
		}

	}

	private String getOperationQuery(String tranType, String assetId) {
		String result = "";
		if (tranType.equalsIgnoreCase("DISPOSAL")) {
			result = "UPDATE AM_ASSETDISPOSAL SET RAISE_ENTRY = 'N' WHERE ASSET_ID ='"
					+ assetId + "'";
		} else if (tranType.equalsIgnoreCase("TRANSFER")) {
			result = "UPDATE AM_ASSETTRANSFER SET RAISE_ENTRY = 'N' WHERE ASSET_ID ='"
					+ assetId + "'";
		} else if (tranType.equalsIgnoreCase("REVALUE")) {
			result = "UPDATE AM_ASSETREVALUE SET RAISE_ENTRY = 'N' WHERE ASSET_ID ='"
					+ assetId + "'";
		} else {
		}
//		System.out.println(result + ".." + tranType + ".." + assetId);
		return result;
	}

	public int countPendingTransaction(String superId) {
		String query = "SELECT ISNULL(count(*),0) FROM am_asset_approval WHERE super_id ='"
				+ superId + "' AND process_status = 'P'";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		ArrayList list = new ArrayList();
		int rowNum = 0;
		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				rowNum = rs.getInt(1);
			}
		} catch (Exception e) {
			String warning = "WARNING:Error getting Number Of Pending Transaction"
					+ " ->" + e.getMessage();
			System.out.println(warning);
		} finally {
			closeConnection(con, ps, rs);
		}
		return rowNum;

	}

	public String getStatusName_old(String status) {

		String result = "";
		if (status.equalsIgnoreCase("A")) {
			result = "APPROVED";
		} else if (status.equalsIgnoreCase("Q")) {
			result = "TIMED-OUT";
		} else if (status.equalsIgnoreCase("P")) {
			result = "PENDING";
		} else if (status.equalsIgnoreCase("R")){
			result = "REJECTED";
		}else{result="PENDING";}
		return result;
	}

    //Added by ayojava-27-07-2009
    public String getStatusName(String status) {

		String result = "";
		if (status.equalsIgnoreCase("A"))
		{
            result = "APPROVED";
		}
		else if (status.equalsIgnoreCase("Q"))
		{
			result = "TIMED-OUT";
		}
		else if (status.equalsIgnoreCase("P"))
		{
			result = "PENDING";
		}
		else if (status.equalsIgnoreCase("R"))
		{
			result = "REJECTED";
		}
		else if (status.equalsIgnoreCase("F"))
		{
			result = "PARTIAL REJECTION";
		}
                else if (status.equalsIgnoreCase("C"))
		{
			result = "CLOSED";
		}
                else if (status.equalsIgnoreCase("RP"))
		{
			result = "REJECTED";
		}
                else if (status.equalsIgnoreCase("FX"))
		{
			result = returnStatusName("FX");
		}
                  else if (status.equalsIgnoreCase("FP"))
		{
			result = returnStatusName("FP");
		}
                  else if (status.equalsIgnoreCase("FR"))
		{
			result = returnStatusName("FR");
		}
                  else if (status.equalsIgnoreCase("PR"))
		{
			result = "PREVIOUS";
		}		
                  else if (status.equalsIgnoreCase("WX"))
		{
			result = returnStatusName("WX");
		}

		else
		{
			result="PENDING";
		}
		return result;
	}

	public void setOperationRaiseEntryStatus(String batchId, String tranType) {
		String updateTranType = "";

		if (tranType.equalsIgnoreCase("DISPOSAL")) {
			updateTranType = "UPDATE AM_ASSETDISPOSAL SET RAISE_ENTRY = ? WHERE ASSET_ID = ?";
		} else if (tranType.equalsIgnoreCase("TRANSFER")) {
			updateTranType = "UPDATE AM_ASSETTRANSFER SET RAISE_ENTRY = ? WHERE ASSET_ID = ?";
		} else if (tranType.equalsIgnoreCase("REVALUATION")) {
			updateTranType = "UPDATE AM_ASSETREVALUE SET RAISE_ENTRY = ? WHERE ASSET_ID = ?";
		} else {
		}

		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(updateTranType);
			ps.setString(1, "Y");
			ps.setString(2, batchId);
			int x = ps.executeUpdate();
		} catch (Exception e) {
			String warning = "WARNING:Error setOperationRaiseEntryStatus(??) "
					+ " ->" + e.getMessage();
			System.out.println(warning);
		} finally {
			closeConnection(con, ps);
		}
	}  

	public String href(String status) {
		StringBuffer result = new StringBuffer(30);
		if (status.equalsIgnoreCase("Q")) {
			result
					.append("<a href= \"javascript:newWindow('<%=tranType%>','<%=assetId_%>')\"><%=userName%></a>");
		} else {
			result.append("<%=userName%>");
		}
		return result.toString();
	}



    public ArrayList findTransactionForApproval(String query_,String order)
    {
       // System.out.println("in method and the query is ............." +query_);
    		

        String SELECT_QUERY = "SELECT DISTINCT CONVERT(VARCHAR(10),a.POSTING_DATE,101),a.description,a.DR_ACCT,a.CR_ACCT," +
        		"a.DR_NARRATION,a.CR_NARRATION,a.AMOUNT,a.POSTING_DATE,b.FULL_NAME," +
        		"a.ASSET_ID,a.transaction_id,a.tran_sent_time,a.tran_type,a.USER_ID, " +
        		"a.transaction_level,a.approval_level_count,a.asset_code,a.RecordType FROM " +
        		"am_asset_approval a JOIN AM_GB_USER b ON a.USER_ID = b.USER_ID "+query_+" "+
        		"UNION "+
        		"SELECT DISTINCT CONVERT(VARCHAR(10),a.POSTING_DATE,101),a.description,a.DR_ACCT,a.CR_ACCT," +
        		"a.DR_NARRATION,a.CR_NARRATION,a.AMOUNT,a.POSTING_DATE,'' AS FULL_NAME," +
        		"a.ASSET_ID,a.transaction_id,a.tran_sent_time,a.tran_type,a.USER_ID, " +
        		"a.transaction_level,a.approval_level_count,a.asset_code,a.RecordType FROM " +
        		"am_asset_approval a JOIN AM_GB_USER b ON a.USER_ID = 0 "+query_+" "+order+"";

 //       System.out.println("SELECT_QUERY >>>>>>>>>>> " + SELECT_QUERY);  
//        System.out.println("status in findTransactionForApproval >>>>>>>>>>> " + status+"    tranType: "+tranType); 
        /*
        String SELECT_QUERY = "SELECT DISTINCT a.DR_ACCT,a.CR_ACCT,"
				+ "a.DR_NARRATION,a.CR_NARRATION,a.AMOUNT,"
				+ "a.POSTING_DATE,"
				//+ "a.DESCRIPTION,"
                + "b.FULL_NAME,a.ASSET_ID"
				+ "FROM am_asset_approval a JOIN AM_GB_USER b "
				+ "ON a.USER_ID = b.USER_ID"
				//+ "WHERE a.BATCH_ID IS NOT NULL"
				+ query_;
				//+ " ORDER BY a.POSTING_DATE DESC";

		//AND SUPER_ID = " + ;


      */
		ArrayList list = new ArrayList();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			con = getConnection("legendPlus");
			ps = con.prepareStatement(SELECT_QUERY);
			// ps.setInt(1,Integer.parseInt(superid));
			rs = ps.executeQuery();

			while (rs.next()) {
				String debitAccount = rs.getString("DR_ACCT");
				String creditAccount = rs.getString("CR_ACCT");
				String creditNarration = rs.getString("CR_NARRATION");
				String debitNarration = rs.getString("DR_NARRATION");
				double amount = rs.getDouble("AMOUNT");
				String userId = rs.getString("USER_ID");
				String recType = rs.getString("RecordType");
				String postingDate = (rs.getString("POSTING_DATE")==null)?"":formatDate(rs.getDate("POSTING_DATE"));
				String transaction_id = (rs.getString("transaction_id")==null)?"":rs.getString("transaction_id");
                String fullName = (rs.getString("FULL_NAME")==null)?"":rs.getString("FULL_NAME");
				String assetId = (rs.getString("ASSET_ID")==null)?"":rs.getString("ASSET_ID");
				String desc =(rs.getString("DESCRIPTION")==null)?"":rs.getString("DESCRIPTION");
				String trans_sent_time =(rs.getString("tran_sent_time")==null)?"":rs.getString("tran_sent_time");
                String tran_type = (rs.getString("tran_type")==null)?"":rs.getString("tran_type");
                //int transaction_level = (rs.getInt("transaction_level")==null)?0:rs.getInt("transaction_level"));
                int transaction_level = rs.getInt("transaction_level");
                int approval_level_count = rs.getInt("approval_level_count");
                int assetCode = rs.getInt("asset_code");

				Transaction tran = new Transaction(debitAccount,
						creditAccount, creditNarration, debitNarration, amount,
						postingDate,fullName, assetId, desc,transaction_id,trans_sent_time,tran_type,transaction_level,approval_level_count);
				tran.setUserId(userId);
                tran.setAssetCode(assetCode);
                tran.setCrAcctType(recType);;
				list.add(tran);

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



 

    public ArrayList findTransactionForApproval(String query_,String fromDate,String toDate,String asset_Id,String tranType,String status, String order)
    {
       // System.out.println("in method and the query is ............." +query_);
    		

        String SELECT_QUERY = "SELECT DISTINCT CONVERT(VARCHAR(10),a.POSTING_DATE,101),a.description,a.DR_ACCT,a.CR_ACCT," +
        		"a.DR_NARRATION,a.CR_NARRATION,a.AMOUNT,a.POSTING_DATE,b.FULL_NAME," +
        		"a.ASSET_ID,a.transaction_id,a.tran_sent_time,a.tran_type,a.USER_ID, " +
        		"a.transaction_level,a.approval_level_count,a.asset_code,a.RecordType FROM " +
        		"am_asset_approval a JOIN AM_GB_USER b ON a.USER_ID = b.USER_ID" +query_ + order;

//        System.out.println("SELECT_QUERY >>>>>>>>>>> " + SELECT_QUERY);  
//        System.out.println("status in findTransactionForApproval >>>>>>>>>>> " + status+"    tranType: "+tranType); 
        /*
        String SELECT_QUERY = "SELECT DISTINCT a.DR_ACCT,a.CR_ACCT,"
				+ "a.DR_NARRATION,a.CR_NARRATION,a.AMOUNT,"
				+ "a.POSTING_DATE,"
				//+ "a.DESCRIPTION,"
                + "b.FULL_NAME,a.ASSET_ID"
				+ "FROM am_asset_approval a JOIN AM_GB_USER b "
				+ "ON a.USER_ID = b.USER_ID"
				//+ "WHERE a.BATCH_ID IS NOT NULL"
				+ query_;
				//+ " ORDER BY a.POSTING_DATE DESC";

		//AND SUPER_ID = " + ;


      */
		ArrayList list = new ArrayList();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

//			con = getConnection("legendPlus");
//			ps = con.prepareStatement(SELECT_QUERY);
//			// ps.setInt(1,Integer.parseInt(superid));
//			rs = ps.executeQuery();
            con = getConnection("legendPlus");
            ps = con.prepareStatement(SELECT_QUERY.toString());
//            System.out.println("<========query_ in findTransactionForApproval=======>: "+query_);
            if(query_.contains("a.PROCESS_STATUS") && query_.contains("TRAN_TYPE")){
            	//System.out.println("<========findTransactionForApproval=======>0");
          	  ps.setString(1, status);
          	  ps.setString(2, tranType);
            }  
            if(query_.contains("a.PROCESS_STATUS") && query_.contains("a.asset_id") && query_.contains("TRAN_TYPE") && !query_.contains("a.POSTING_DATE")){
            	//System.out.println("<========findTransactionForApproval=======>3"+"   status: "+status+"   asset_Id: "+asset_Id+"    tranType: "+tranType);
            	  ps.setString(1, status);
            	  ps.setString(2, asset_Id);
                  ps.setString(3, tranType);
              }
            if(query_.contains("a.PROCESS_STATUS") && query_.contains("a.POSTING_DATE") && query_.contains("a.asset_id") && query_.contains("TRAN_TYPE")){
            	//System.out.println("<========findTransactionForApproval=======>5");
          	  ps.setString(1, status);
          	  ps.setString(2, fromDate);
              ps.setString(3, toDate);
          	  ps.setString(4, asset_Id);
              ps.setString(5, tranType);
            }
            if(query_.contains("a.PROCESS_STATUS") && query_.contains("a.POSTING_DATE") && query_.contains("TRAN_TYPE") && !query_.contains("a.asset_id")){
            	//System.out.println("<========findTransactionForApproval=======>4");
          	  ps.setString(1, status);
          	  ps.setString(2, fromDate);
              ps.setString(3, toDate);
              ps.setString(4, tranType);
            }
            rs = ps.executeQuery();
			while (rs.next()) {
				String debitAccount = rs.getString("DR_ACCT");
				String creditAccount = rs.getString("CR_ACCT");
				String creditNarration = rs.getString("CR_NARRATION");
				String debitNarration = rs.getString("DR_NARRATION");
				double amount = rs.getDouble("AMOUNT");
				String userId = rs.getString("USER_ID");
				String recType = rs.getString("RecordType");
				String postingDate = (rs.getString("POSTING_DATE")==null)?"":formatDate(rs.getDate("POSTING_DATE"));
				String transaction_id = (rs.getString("transaction_id")==null)?"":rs.getString("transaction_id");
                String fullName = (rs.getString("FULL_NAME")==null)?"":rs.getString("FULL_NAME");
				String assetId = (rs.getString("ASSET_ID")==null)?"":rs.getString("ASSET_ID");
				String desc =(rs.getString("DESCRIPTION")==null)?"":rs.getString("DESCRIPTION");
				String trans_sent_time =(rs.getString("tran_sent_time")==null)?"":rs.getString("tran_sent_time");
                String tran_type = (rs.getString("tran_type")==null)?"":rs.getString("tran_type");
                //int transaction_level = (rs.getInt("transaction_level")==null)?0:rs.getInt("transaction_level"));
                int transaction_level = rs.getInt("transaction_level");
                int approval_level_count = rs.getInt("approval_level_count");
                int assetCode = rs.getInt("asset_code");

				Transaction tran = new Transaction(debitAccount,
						creditAccount, creditNarration, debitNarration, amount,
						postingDate,fullName, assetId, desc,transaction_id,trans_sent_time,tran_type,transaction_level,approval_level_count);
				tran.setUserId(userId);
                tran.setAssetCode(assetCode);
                tran.setCrAcctType(recType);;
				list.add(tran);

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


    public String getStatusName(String status,String tranId,String transType, String postingRecord) {
    ApprovalRecords ar = new ApprovalRecords();
    String filter = "";
    ArrayList aList = new ArrayList();
		String result = "";
		if (status.equalsIgnoreCase("A"))
		{   filter= " AND CONVERT(varchar, TRANS_ID) = ? ";
                   aList= ar.findPostedEntry(filter, tranId);
                    if(aList.size() > 0){
                    result = "POSTED";
                    }else if(transType.equalsIgnoreCase("Asset Update") || transType.equalsIgnoreCase("Bulk Asset Update")){
                    result = "APPROVED";

                    }else{

                     result = "WAITING FOR POSTING";
                    }
                    if (status.equalsIgnoreCase("A") && postingRecord.equals(""))
            		{result = "PROCESSED";}

		}
		else if (status.equalsIgnoreCase("Q"))
		{
			result = "TIMED-OUT";
		}
		else if (status.equalsIgnoreCase("P"))
		{
			result = "PENDING";
		}
		else if (status.equalsIgnoreCase("R"))
		{
			result = "REJECTED";
		}
		else if (status.equalsIgnoreCase("F"))
		{
			result = "PARTIAL REJECTION";
		}
                else if (status.equalsIgnoreCase("C"))
		{
			result = "CLOSED";
		}
                else if (status.equalsIgnoreCase("RP"))
		{
			result = "REJECTED";
		}
                else if (status.equalsIgnoreCase("PR"))
		{
			result = "PREVIOUS REJECTION";
		}	
                else if (status.equalsIgnoreCase("FX"))
		{
			result = returnStatusName("FX");
		}
                  else if (status.equalsIgnoreCase("FP"))
		{
			result = returnStatusName("FP");
		}
                  else if (status.equalsIgnoreCase("FR"))
		{
			result = returnStatusName("FR");
		}
                  else if (status.equalsIgnoreCase("WX"))
		{
			result = returnStatusName("WX");
		}

                  else if (status.equalsIgnoreCase("WA"))
		{
			result = returnStatusName("WA");
		}
                  else if (status.equalsIgnoreCase("FD"))
		{
			result = returnStatusName("FD");
		}

                 else if (status.equalsIgnoreCase("WCA"))
		{
			result = returnStatusName("WCA");
		} else if (status.equalsIgnoreCase("WCX"))
		{
			result = returnStatusName("WCX");
		} else if (status.equalsIgnoreCase("WCR"))
		{
			result = returnStatusName("WCR");
		} else if (status.equalsIgnoreCase("WCRI"))
		{
			result = returnStatusName("WCRI");
		} else if (status.equalsIgnoreCase("WCFP"))
		{
			result = returnStatusName("WCFP");
		}

                else if (status.equalsIgnoreCase("WCA"))
		{
			result = returnStatusName("WCA");
		}
                 else if (status.equalsIgnoreCase("WCX"))
		{
			result = returnStatusName("WCX");
		}
                 else if (status.equalsIgnoreCase("WCR"))
		{
			result = returnStatusName("WCR");
		}
                 else if (status.equalsIgnoreCase("WCRI"))
		{
			result = returnStatusName("WCRI");
		}
                 else if (status.equalsIgnoreCase("WCFP"))
		{
			result = returnStatusName("WCFP");
		}
		else if (status.equalsIgnoreCase("WCIB"))
		{
			result = returnStatusName("WCIB");
		}

		else
		{
			result="PENDING";
		}
		return result;
	}



    public ArrayList findAssetVerificationForApproval(String query_)
    {
       // System.out.println("in method and the query is ............." +query_);


        String SELECT_QUERY = "SELECT DISTINCT description,DR_ACCT,CR_ACCT," +
        		"DR_NARRATION,CR_NARRATION,AMOUNT,POSTING_DATE," +
        		"ASSET_ID,transaction_id,tran_sent_time,tran_type,USER_ID, " +
        		"transaction_level,approval_level_count,asset_code FROM am_asset_approval where batch_id in (SELECT BATCH_ID FROM am_gb_workbookupdate  " +
        		"WHERE PROCESS_STATUS = 'APPROVED') AND PROCESS_STATUS = 'A'" +query_;

//        System.out.println("SELECT_QUERY >>>>>>>>>>> " + SELECT_QUERY);

        /*
        String SELECT_QUERY = "SELECT DISTINCT a.DR_ACCT,a.CR_ACCT,"
				+ "a.DR_NARRATION,a.CR_NARRATION,a.AMOUNT,"
				+ "a.POSTING_DATE,"
				//+ "a.DESCRIPTION,"
                + "b.FULL_NAME,a.ASSET_ID"
				+ "FROM am_asset_approval a JOIN AM_GB_USER b "
				+ "ON a.USER_ID = b.USER_ID"
				//+ "WHERE a.BATCH_ID IS NOT NULL"
				+ query_;
				//+ " ORDER BY a.POSTING_DATE DESC";

		//AND SUPER_ID = " + ;


      */
		ArrayList list = new ArrayList();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			con = getConnection("legendPlus");
			ps = con.prepareStatement(SELECT_QUERY);
			// ps.setInt(1,Integer.parseInt(superid));
			rs = ps.executeQuery();

			while (rs.next()) {

				//String id = rs.getString("TRANSACTION_ID");
				// System.out.println(id);
				String debitAccount = rs.getString("DR_ACCT");
				String creditAccount = rs.getString("CR_ACCT");
				//String drAcctType = rs.getString("DR_ACCT_TYPE");
				//String crAcctType = rs.getString("CR_ACCT_TYPE");
				//String drTranCode = rs.getString("DR_TRAN_CODE");
				//String crTranCode = rs.getString("CR_TRAN_CODE");
				String creditNarration = rs.getString("CR_NARRATION");
				String debitNarration = rs.getString("DR_NARRATION");
				double amount = rs.getDouble("AMOUNT");
				String userId = rs.getString("USER_ID");
				//String superId = rs.getString("SUPER_ID");
				//String legacyCode = rs.getString("LEGACY_ID");
				//String code = "";// rs.getString("TRAN_CODE");
				String postingDate = (rs.getString("POSTING_DATE")==null)?"":formatDate(rs.getDate("POSTING_DATE"));
				//String effectiveDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
				//String status = rs.getString("PROCESS_STATUS");
				//String supervisor = rs.getString("SUPERVISOR");
				//String rejectReason = rs.getString("REJECT_REASON");
				//String batchId = rs.getString("BATCH_ID");,a.transaction_id
				String transaction_id = (rs.getString("transaction_id")==null)?"":rs.getString("transaction_id");
   //             String fullName = (rs.getString("FULL_NAME")==null)?"":rs.getString("FULL_NAME");
				String assetId = (rs.getString("ASSET_ID")==null)?"":rs.getString("ASSET_ID");
				//int branchId = rs.getInt("BRANCH_ID");
				//int deptId = rs.getInt("DEPT_ID");
				//int categoryId = rs.getInt("CATEGORY_ID");
				//String regNo = rs.getString("REGISTRATION_NO");
				String desc =(rs.getString("DESCRIPTION")==null)?"":rs.getString("DESCRIPTION");
				String trans_sent_time =(rs.getString("tran_sent_time")==null)?"":rs.getString("tran_sent_time");
                String tran_type = (rs.getString("tran_type")==null)?"":rs.getString("tran_type");
                //int transaction_level = (rs.getInt("transaction_level")==null)?0:rs.getInt("transaction_level"));
                int transaction_level = rs.getInt("transaction_level");
                int approval_level_count = rs.getInt("approval_level_count");
                int assetCode = rs.getInt("asset_code");
                // double cost = rs.getDouble("COST_PRICE");
				// String assetUser = rs.getString("ASSET_USER");
				//String assetStatus = rs.getString("ASSET_STATUS");
				//String tranType = rs.getString("TRAN_TYPE");
				//String sentTime = rs.getString("TRAN_SENT_TIME");
 
				Transaction tran = new Transaction(debitAccount,
						creditAccount, creditNarration, debitNarration, amount,
						postingDate,"", assetId, desc,transaction_id,trans_sent_time,tran_type,transaction_level,approval_level_count);
				tran.setUserId(userId);
                                tran.setAssetCode(assetCode);
				list.add(tran);

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


    public ArrayList findAssetProofForProcessing(String query_)
    {
       // System.out.println("in method and the query is ............." +query_);


        String SELECT_QUERY = "SELECT DISTINCT b.BRANCH_ID,a.description,DR_ACCT,CR_ACCT,DR_NARRATION,CR_NARRATION,COALESCE(AMOUNT,0) AS AMOUNT," +
        		"POSTING_DATE,a.ASSET_ID,transaction_id,tran_sent_time,tran_type,a.USER_ID,SUPER_ID," +
        		"transaction_level,approval_level_count,a.asset_code,b.BATCH_ID,b.GROUP_ID,CAST(POSTING_DATE AS date)  " +
        		"FROM am_asset_approval a,am_Asset_Proof b " +
        		"where a.batch_id = b.BATCH_ID and b.PROCESS_STATUS = 'APPROVED' " +query_;

        //System.out.println("SELECT_QUERY in findAssetProofForProcessing >>>>>>>>>>> " + SELECT_QUERY);
  
		ArrayList list = new ArrayList();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
 
			con = getConnection("legendPlus");
			ps = con.prepareStatement(SELECT_QUERY);
			// ps.setInt(1,Integer.parseInt(superid));
			rs = ps.executeQuery();

			while (rs.next()) {

				String branchId = rs.getString("BRANCH_ID");
				// System.out.println(id);
				String groupId = rs.getString("GROUP_ID");
				String debitAccount = rs.getString("DR_ACCT");
				String creditAccount = rs.getString("CR_ACCT");
				//String drAcctType = rs.getString("DR_ACCT_TYPE");
				//String crAcctType = rs.getString("CR_ACCT_TYPE");
				//String drTranCode = rs.getString("DR_TRAN_CODE");
				//String crTranCode = rs.getString("CR_TRAN_CODE");
				String creditNarration = rs.getString("CR_NARRATION");
				String debitNarration = rs.getString("DR_NARRATION");
				double amount = rs.getDouble("AMOUNT");
				String userId = rs.getString("USER_ID");
				String superId = rs.getString("SUPER_ID");
				//String legacyCode = rs.getString("LEGACY_ID");
				//String code = "";// rs.getString("TRAN_CODE");
				String postingDate = (rs.getString("POSTING_DATE")==null)?"":formatDate(rs.getDate("POSTING_DATE"));
				//String effectiveDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
				//String status = rs.getString("PROCESS_STATUS");
				//String supervisor = rs.getString("SUPERVISOR");
				//String rejectReason = rs.getString("REJECT_REASON");
				//String batchId = rs.getString("BATCH_ID");,a.transaction_id
				String transaction_id = (rs.getString("transaction_id")==null)?"":rs.getString("transaction_id");
   //             String fullName = (rs.getString("FULL_NAME")==null)?"":rs.getString("FULL_NAME");
				String assetId = (rs.getString("ASSET_ID")==null)?"":rs.getString("ASSET_ID");
				//int branchId = rs.getInt("BRANCH_ID");
				//int deptId = rs.getInt("DEPT_ID");
				//int categoryId = rs.getInt("CATEGORY_ID");
				//String regNo = rs.getString("REGISTRATION_NO");
				String desc =(rs.getString("DESCRIPTION")==null)?"":rs.getString("DESCRIPTION");
				String trans_sent_time =(rs.getString("tran_sent_time")==null)?"":rs.getString("tran_sent_time");
                String tran_type = (rs.getString("tran_type")==null)?"":rs.getString("tran_type");
                //int transaction_level = (rs.getInt("transaction_level")==null)?0:rs.getInt("transaction_level"));
                int transaction_level = rs.getInt("transaction_level");
                int approval_level_count = rs.getInt("approval_level_count");
                int assetCode = rs.getInt("asset_code");
                // double cost = rs.getDouble("COST_PRICE");
				// String assetUser = rs.getString("ASSET_USER");
				//String assetStatus = rs.getString("ASSET_STATUS");
				//String tranType = rs.getString("TRAN_TYPE");
				//String sentTime = rs.getString("TRAN_SENT_TIME");
 
				Transaction tran = new Transaction(debitAccount,
						creditAccount, creditNarration, debitNarration, amount,
						postingDate,"", assetId, desc,transaction_id,trans_sent_time,tran_type,transaction_level,approval_level_count);
				tran.setUserId(userId);
                tran.setAssetCode(assetCode);
                tran.setSuperId(superId);
                tran.setBatchId(groupId); 
                tran.setBranchId(Integer.parseInt(branchId));
				list.add(tran);

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


  public String returnStatusName(String statusCode)
    {
    	String statusNameQry="select status_name from am_gb_status where status_code=?";

    	String statusName=aprecords.getCodeName(statusNameQry,statusCode);

    	return statusName;
    }


  public String getListStatusName(String status,int tranId,String transType) {
  ApprovalRecords ar = new ApprovalRecords();
  String filter = "";
  ArrayList aList = new ArrayList();
		String result = "";
		if (status.equalsIgnoreCase("A"))
		{   filter= " AND TRANS_ID = ? ";
                 aList= ar.findPostedEntry(filter, Integer.toString(tranId));
                  if(aList.size() > 0){ 
                  result = "POSTED";
                  }else if(transType.equalsIgnoreCase("Asset Update") || transType.equalsIgnoreCase("Bulk Asset Update")){
                  result = "APPROVED";

                  }else{
                   result = "WAITING FOR ISSUANCE";
                  }
//                  result = "Issued Out";
		}
		else if (status.equalsIgnoreCase("I"))
		{
			result = "Issued Out";
		}
		else if (status.equalsIgnoreCase("Q"))
		{
			result = "TIMED-OUT";
		}
		else if (status.equalsIgnoreCase("P"))
		{
			result = "PENDING";
		}
		else if (status.equalsIgnoreCase("R"))
		{
			result = "REJECTED";
		}
		else if (status.equalsIgnoreCase("F"))
		{
			result = "PARTIAL REJECTION";
		}
              else if (status.equalsIgnoreCase("C"))
		{
			result = "CLOSED";
		}
              else if (status.equalsIgnoreCase("AP"))
		{
			result = "APPROVED";
		}		
              else if (status.equalsIgnoreCase("RP"))
		{
			result = "REJECTED";
		}

                 else if (status.equalsIgnoreCase("FX"))
		{
			result = returnStatusName("FX");
		}
                else if (status.equalsIgnoreCase("FP"))
		{
			result = returnStatusName("FP");
		}
                else if (status.equalsIgnoreCase("FR"))
		{
			result = returnStatusName("FR");
		}
                else if (status.equalsIgnoreCase("WX"))
		{
			result = returnStatusName("WX");
		}

                else if (status.equalsIgnoreCase("WA"))
		{
			result = returnStatusName("WA");
		}
                else if (status.equalsIgnoreCase("FD"))
		{
			result = returnStatusName("FD");
		}

               else if (status.equalsIgnoreCase("WCA"))
		{
			result = returnStatusName("WCA");
		} else if (status.equalsIgnoreCase("WCX"))
		{
			result = returnStatusName("WCX");
		} else if (status.equalsIgnoreCase("WCR"))
		{
			result = returnStatusName("WCR");
		} else if (status.equalsIgnoreCase("WCRI"))
		{
			result = returnStatusName("WCRI");
		} else if (status.equalsIgnoreCase("WCFP"))
		{
			result = returnStatusName("WCFP");
		}

              else if (status.equalsIgnoreCase("WCA"))
		{
			result = returnStatusName("WCA");
		}
               else if (status.equalsIgnoreCase("WCX"))
		{
			result = returnStatusName("WCX");
		}
               else if (status.equalsIgnoreCase("WCR"))
		{
			result = returnStatusName("WCR");
		}
               else if (status.equalsIgnoreCase("WCRI"))
		{
			result = returnStatusName("WCRI");
		}
               else if (status.equalsIgnoreCase("WCFP"))
		{
			result = returnStatusName("WCFP");
		}
		else if (status.equalsIgnoreCase("WCIB"))
		{
			result = returnStatusName("WCIB");
		}

		else
		{
			result="PENDING";
		}
		return result;
	}


public ArrayList findAllRequisitionDetails(String groupId) {

	String query="select * from FM_REQUISITION_DETAILS where GROUP_ID='"+groupId+"'";

	//System.out.println("findAllRequisition query: "+query);
	ArrayList _list = new ArrayList();
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	try 
	{
		con = getConnection("legendPlus");
		ps = con.prepareStatement(query);
		rs = ps.executeQuery();
		while(rs.next())
		{ 
			String barCode = rs.getString("Bar_Code");
			String assetId = rs.getString("Asset_id");
			String description = rs.getString("ASSET_DESCRIPTION");
			
			Transaction tran = new Transaction(assetId, description,barCode);
			
			_list.add(tran);
		}
	} 
	catch (Exception e) 
	{
	System.out.println(this.getClass().getName()+ " Error findAllRequisitionDetails ->" + e.getMessage());
	}
	
	finally 
	{
	closeConnection(con, ps, rs);
	}
			return _list;
			
}

// get transaction status for user
public ArrayList findProcessingRejectionTransactionList(String queryFilter) {

	String SELECT_QUERY = "SELECT DISTINCT a.BATCH_ID,a.TRANSACTION_ID,a.DR_ACCT,CR_ACCT,a.DR_ACCT_TYPE,a.CR_ACCT_TYPE,"
				+ "a.DR_TRAN_CODE,a.CR_TRAN_CODE,a.DR_NARRATION,a.CR_NARRATION,a.AMOUNT,a.USER_ID,"
				+ "a.SUPER_ID,a.LEGACY_ID,a.POSTING_DATE AS POSTING_DATE,"
				+ "a.EFFECTIVE_DATE,a.PROCESS_STATUS,a.SUPER_ID,"
				+ "a.REJECT_REASON,a.TRAN_TYPE,a.TRAN_SENT_TIME,"
				+ "b.FULL_NAME,a.ASSET_ID,c.BRANCH_ID,c.DEPT_ID,"
				+ "c.CATEGORY_ID,c.REGISTRATION_NO,a.DESCRIPTION,c.ASSET_STATUS,a.asset_code,a.PROCESSING "
				+ "FROM am_asset_approval a JOIN AM_GB_USER b "
				+ "ON a.SUPER_ID = b.USER_ID  LEFT JOIN AM_ASSET c "
				+ "ON (a.ASSET_ID=c.ASSET_ID)  "
				+ "WHERE a.BATCH_ID IS NOT NULL AND (a.PROCESS_STATUS = 'P' OR a.PROCESS_STATUS = 'F' OR a.PROCESS_STATUS = 'A'  OR a.PROCESS_STATUS = 'U' "
				+ "OR a.PROCESS_STATUS = 'Q'  OR a.PROCESS_STATUS = 'C' OR a.PROCESS_STATUS = 'PR') AND a.PROCESSING = 'Processing Rejection' "+queryFilter+"  and a.posting_date between getdate()-60 and getdate() "
				+ " UNION "
				+"SELECT DISTINCT a.BATCH_ID,a.TRANSACTION_ID,a.DR_ACCT,CR_ACCT,a.DR_ACCT_TYPE,a.CR_ACCT_TYPE,"
				+ "a.DR_TRAN_CODE,a.CR_TRAN_CODE,a.DR_NARRATION,a.CR_NARRATION,a.AMOUNT,a.USER_ID,"
				+ "a.SUPER_ID,a.LEGACY_ID,a.DATE_APPROVED AS POSTING_DATE,"
				+ "a.EFFECTIVE_DATE,a.PROCESS_STATUS,a.SUPER_ID,"
				+ "a.REJECT_REASON,a.TRAN_TYPE,a.TRAN_SENT_TIME,"
				+ "b.FULL_NAME,a.ASSET_ID,c.BRANCH_ID,c.DEPT_ID,"
				+ "c.CATEGORY_ID,c.REGISTRATION_NO,a.DESCRIPTION,c.ASSET_STATUS,a.asset_code,a.PROCESSING "
				+ "FROM am_asset_approval a JOIN AM_GB_USER b "
				+ "ON a.SUPER_ID = b.USER_ID  LEFT JOIN AM_ASSET c " 
				+ "ON (a.ASSET_ID=c.ASSET_ID)  "
				+ "WHERE a.BATCH_ID IS NOT NULL AND (a.PROCESS_STATUS = 'R' OR a.PROCESS_STATUS = 'RP') AND a.PROCESSING = 'Processing Rejection' "+queryFilter+" and a.posting_date between getdate()-60 and getdate() and a.DATE_APPROVED between getdate()-60 and getdate() "
				+ "ORDER BY POSTING_DATE DESC";		

    //System.out.println("SELECT_QUERY >>>>>>>" + SELECT_QUERY);
	ArrayList list = new ArrayList();
	Connection con = null;
	PreparedStatement ps = null; 
	ResultSet rs = null;
	try {

		con = getConnection("legendPlus");
		ps = con.prepareStatement(SELECT_QUERY);
		// ps.setInt(1,Integer.parseInt(superid));
		rs = ps.executeQuery();

		while (rs.next()) {

			String id = rs.getString("TRANSACTION_ID");
			// System.out.println(id);
			String debitAccount = rs.getString("DR_ACCT");
			String creditAccount = rs.getString("CR_ACCT");
			String drAcctType = rs.getString("DR_ACCT_TYPE");
			String crAcctType = rs.getString("CR_ACCT_TYPE");
			String drTranCode = rs.getString("DR_TRAN_CODE");
			String crTranCode = rs.getString("CR_TRAN_CODE");
			String creditNarration = rs.getString("CR_NARRATION");
			String debitNarration = rs.getString("DR_NARRATION");
			double amount = rs.getDouble("AMOUNT");
			String userId = rs.getString("USER_ID");
			String superId = rs.getString("SUPER_ID");
			String legacyCode = rs.getString("LEGACY_ID");
			String code = "";// rs.getString("TRAN_CODE");
			String postingDate = formatDate(rs.getDate("POSTING_DATE"));
			String effectiveDate = formatDate(rs.getDate("EFFECTIVE_DATE"));
			String status = rs.getString("PROCESS_STATUS");
			String supervisor = rs.getString("SUPER_ID");
			String rejectReason = rs.getString("REJECT_REASON");
			String batchId = rs.getString("BATCH_ID");
			String fullName = rs.getString("FULL_NAME");
			String assetId = rs.getString("ASSET_ID");
			int branchId = rs.getInt("BRANCH_ID");
			int deptId = rs.getInt("DEPT_ID");
			int categoryId = rs.getInt("CATEGORY_ID");
			String regNo = rs.getString("REGISTRATION_NO");
			String desc = rs.getString("DESCRIPTION");
			// double cost = rs.getDouble("COST_PRICE");
			String processing = rs.getString("PROCESSING");
			String assetStatus = rs.getString("ASSET_STATUS");
			String tranType = rs.getString("TRAN_TYPE");
			String sentTime = rs.getString("TRAN_SENT_TIME");
            int assetCode = rs.getInt("asset_code");
			Transaction tran = new Transaction(id, debitAccount,
					creditAccount, creditNarration, debitNarration, amount,
					userId, superId, legacyCode, code, postingDate,
					effectiveDate, status, supervisor, rejectReason,
					batchId, fullName, assetId, drAcctType, crAcctType,
					drTranCode, crTranCode, branchId, deptId, categoryId,
					regNo, desc, assetStatus, tranType, sentTime);
                    tran.setAssetCode(assetCode);
            tran.setProcessing(processing);
			list.add(tran);

		}

	} catch (Exception e) {
		String warning = "WARNING:Error Fetching Transaction for Approval"
				+ " ->" + e.getMessage();
		System.out.println(warning);
	} finally {
		closeConnection(con, ps, rs);
	}
	return list;
}

public ArrayList findPendingTransactionsList(String filterquery,String assetid) {

	String SELECT_QUERY = "SELECT *FROM PENDINGTRANSACTIONS "+filterquery;		
//  String SELECT_QUERY = "SELECT *FROM PENDINGTRANSACTIONS"+filterquery;	
    //System.out.println("SELECT_QUERY >>>>>>>" + SELECT_QUERY+"   assetid: "+assetid);
	ArrayList list = new ArrayList();
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	try {

//		con = getConnection("legendPlus");
//		ps = con.prepareStatement(SELECT_QUERY);
//		rs = ps.executeQuery();
        con = getConnection("legendPlus");
        ps = con.prepareStatement(SELECT_QUERY.toString());
        if(filterquery.contains("ASSET_ID")){
        	//System.out.println("=======cost_price ======");
      	  ps.setString(1, assetid);
        }            
        rs = ps.executeQuery(); 
		while (rs.next()) {

			String id = rs.getString("ID");
			String assetId = rs.getString("ASSET_ID");
			 //System.out.println(id);
			String assetTransfer = rs.getString("ASSET_TRANSFER");
			String finaclePost = rs.getString("FINACLE_POST");
			String improvement = rs.getString("IMPROVEMENT");
			String approval = rs.getString("APPROVAL");
			String disposal = rs.getString("DISPOSAL");
			String acceleDepr = rs.getString("AcceleDepr");
			String tranType = rs.getString("TRANTYPE");
			Transaction tran = new Transaction(id,assetId, assetTransfer,finaclePost,
		    improvement, disposal,approval, tranType,acceleDepr);
			list.add(tran);

		}

	} catch (Exception e) {
		String warning = "WARNING:Error Fetching Pending Transaction for Releasing"
				+ " ->" + e.getMessage();
		System.out.println(warning);
	} finally {
		closeConnection(con, ps, rs);
	}
	return list;
}
  

public ArrayList findPendingTransactionsRecord(String filterquery) {
		
	String SELECT_QUERY = " SELECT Transfer_Id AS TRANS_ID, a.Asset_Id,a.Description,Transfer_Date AS Transaction_Date,'' AS creditAccount,'' AS decitAccount,'Asset Transfer' AS page1,a.Cost_Price AS Amount,'0' AS AssetCode,'Asset Transfer' AS transType FROM am_assetTransfer a, AM_ASSET b WHERE a.Asset_Id = b.Asset_Id AND approval_status != 'ACTIVE' AND approval_status != 'REJECTED' " 
			+" and a.asset_id = '"+filterquery+"' " 
			+" UNION "
			+" SELECT Revalue_Id AS TRANS_ID, a.Asset_Id,b.Description,Revalue_Date AS Transaction_Date,'' AS creditAccount,'' AS decitAccount,'Asset Improvement' AS page1,a.Cost_Price AS Amount,a.Asset_Code,'Asset Improvement' AS transType FROM am_asset_improvement a,AM_ASSET b  WHERE a.Asset_Id = b.Asset_Id AND approval_status IS NULL " 
			+"  and a.asset_id = '"+filterquery+"' " 
			+" UNION "
			+" SELECT TRANS_ID, Asset_Id,description,Transaction_Date,creditAccount,debitAccount,page1,Amount,Asset_Code,PAGE1 AS transType FROM am_Raisentry_Transaction WHERE ISO != '000' " 
			+" and asset_id = '"+filterquery+"' " 
			+" UNION " 
			+" SELECT Transaction_Id AS TRANS_ID, Asset_Id,Description,Posting_Date AS Transaction_Date,'' AS creditAccount,'' AS decitAccount,'Asset Approval' AS page1,0 AS Amount,Asset_Code,'Asset Approval' AS transType FROM am_asset_approval WHERE process_status = 'P' " 
			+" and asset_id = '"+filterquery+"' " 
			+" UNION "
			+" SELECT Disposal_Id AS TRANS_ID,a.Asset_Id,b.Description,Disposal_Date AS Transaction_Date,'' AS creditAccount,'' AS decitAccount,'Asset Disposal' AS page1,Disposal_Amount AS Amount,a.Asset_Code,'Asset Disposal' AS transType FROM am_AssetDisposal a, AM_ASSET b WHERE a.Asset_Id = b.Asset_Id AND disposal_status != 'R' AND DISPOSAL_PERCENT = 100 "
			+"  and a.asset_id = '"+filterquery+"' " ;
	
//    System.out.println("SELECT_QUERY >>>>>>>" + SELECT_QUERY);
	ArrayList list = new ArrayList();
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	try {

		con = getConnection("legendPlus");
		ps = con.prepareStatement(SELECT_QUERY);
		rs = ps.executeQuery();

		while (rs.next()) {

			String id = rs.getString("TRANS_ID");
			String assetId = rs.getString("ASSET_ID");
			String description = rs.getString("DESCRIPTION");
			String debitAccount = rs.getString("CREDITACCOUNT");
			String creditAccount = rs.getString("CREDITACCOUNT");
			double amount = rs.getDouble("AMOUNT");
			String transactionDate = rs.getString("TRANSACTION_DATE");
			String tranType = rs.getString("transType");
//			int assetCode = rs.getInt("ASSET_CODE");
			String fullName = rs.getString("PAGE1");
			
//			System.out.println("=====>description: "+description+"  debitAccount:"+debitAccount+"  amount: "+amount);
			Transaction tran = new Transaction(id,assetId, description,debitAccount,
			creditAccount, amount,transactionDate, tranType,fullName);
			list.add(tran);
//			System.out.println("=====>description: "+description+"  debitAccount:"+debitAccount+"   creditAccount: "+creditAccount+"  amount: "+amount);
		}

	} catch (Exception e) {
		String warning = "WARNING:Error Fetching Pending Transaction for Releasing"
				+ " ->" + e.getMessage();
		System.out.println(warning);
	} finally {
		closeConnection(con, ps, rs);
	}
	return list;
}


}