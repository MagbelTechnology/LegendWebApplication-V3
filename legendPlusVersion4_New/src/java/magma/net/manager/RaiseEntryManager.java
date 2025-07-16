package magma.net.manager;

import java.sql.*;

import magma.net.dao.MagmaDBConnection;

public class RaiseEntryManager extends MagmaDBConnection {
	public RaiseEntryManager() {
	}
 
	// get disposal account
	public String getPLDisposalGLAcct() {
		return getCompanyInfo()[6];
	}

	public String getDisposalSuspenseAcct() {
		return getCompanyInfo()[7];
	}

	// getCompany
	public String[] getCompanyInfo() {
		String[] result = new String[8];

		String query = "SELECT vat_rate,wht_rate,vat_account,wht_account,sbu_required"
				+ ",sbu_level,pl_disposal_account,suspense_acct"
				+ " FROM am_gb_company ";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				result[0] = rs.getString("vat_rate");
				result[1] = rs.getString("wht_rate");
				result[2] = rs.getString("vat_account");
				result[3] = rs.getString("wht_account");
				result[4] = rs.getString("sbu_required");
				result[5] = rs.getString("sbu_level");
				result[6] = rs.getString("pl_disposal_account");
				result[7] = rs.getString("suspense_acct");
			}
		} catch (Exception e) {
			String warning = "WARNING:Error Fetching Company Details" + " ->"
					+ e.getMessage();
			System.out.println(warning);
		} finally {
			closeConnection(con, ps, rs);
		}

		return result;
	}

	// GL Prefix
	private String getGLPrefix(String query, String prefixType) {
		String result = "";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				result = rs.getString("GL_PREFIX");
			}
		} catch (Exception e) {
			String warning = "WARNING:Error Fetching " + prefixType
					+ " GL Prefix" + " ->" + e.getMessage();
			System.out.println(warning);
		} finally {
			closeConnection(con, ps, rs);
		}

		return result;
	}

	// get GL Accounts
	private String getGLAccount(int categoryId, String acctType) {
		String query = "SELECT INSURANCE_ACCT,FUEL_LEDGER,LICENSE_LEDGER,ACCIDENT_LEDGER," +
				"ACCUM_DEP_LEDGER,ASSET_LEDGER,Dep_ledger"
				+ " FROM AM_AD_CATEGORY WHERE CATEGORY_ID = '"
				+ categoryId
				+ "'";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String[] result = new String[7];
		String acctNo = "";

		try {
			con = getConnection("legendPlus"); // connect.getConnection("jdbc:sqlserver://localhost:1433;database=legend","legend","legend");
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				result[0] = rs.getString(1); // insurLegger
				result[1] = rs.getString(2); // fuelLedger
				result[2] = rs.getString(3); // licenseLedger
				result[3] = rs.getString(4); // accidentLedger
				result[4] = rs.getString(5); // accum_dep_ledger
				result[5] = rs.getString(6); // asset_ledger
				result[6] = rs.getString(7); // dep_ledger
			}
		} catch (Exception e) {
			String warning = "WARNING:Error Fetching GL Account" + " ->"
					+ e.getMessage();
			System.out.println(warning);
		} finally {
			closeConnection(con, ps, rs);
		}
		if (acctType.equalsIgnoreCase("INSURANCE")) {
			acctNo = result[0];
		} else if (acctType.equalsIgnoreCase("FUEL")) {
			acctNo = result[1];
		} else if (acctType.equalsIgnoreCase("LICENCE")) {
			acctNo = result[2];
		} else if (acctType.equalsIgnoreCase("ACCIDENT")) {
			acctNo = result[3];
		} else if (acctType.equalsIgnoreCase("ACCUM_DEP")) {
			acctNo = result[4];
		} else if (acctType.equalsIgnoreCase("DEP")) {
			acctNo = result[6];
		} else {
			acctNo = result[5];
		}
		return acctNo;
	}

	public String processGLAccount(String assetId, int categoryId,
			int branchId, int deptId, int sectionId, String acctType) {
		String[] compInfo = getCompanyInfo();
		String sbuRequired = compInfo[4].trim();
		String sbuLevel = (compInfo[5]==null?"":compInfo[5].trim());
		String contAcct = "";
		String query = "";
//		System.out.println("====assetId in processGLAccount: "+assetId+"   categoryId: "+categoryId+"  branchId: "+branchId+"  deptId: "+deptId+"  sectionId: "+sectionId+"  acctType: "+acctType);
//		System.out.println("====sbuRequired in processGLAccount: "+sbuRequired+"  sbuLevel: "+sbuLevel);
		// Sector/Units
		if (sbuRequired.equalsIgnoreCase("Y")) {
			if (sbuLevel.equalsIgnoreCase("Department")) {
				query = "SELECT GL_PREFIX " + " FROM sbu_branch_dept  WHERE "
						+ " DEPTID = '" + deptId + "'  AND  BRANCHID = '"
						+ branchId + "'";
				contAcct = getGLPrefix(query, "DEPARTMENT")
						+ getGLAccount(categoryId, acctType);
			}
			if (sbuLevel.equalsIgnoreCase("Sector/Units")) {
				query = "SELECT GL_PREFIX " + "FROM sbu_dept_section "
						+ "WHERE DEPTID = '" + deptId
						+ "'  AND  BRANCHID = '" + branchId
						+ "' AND SECTIONID ='" + sectionId + "'";
				contAcct = getGLPrefix(query, "SECTION")
						+ getGLAccount(categoryId, acctType);
			}
		} else {
			query = "SELECT GL_PREFIX FROM AM_AD_BRANCH WHERE BRANCH_ID = '"
					+ branchId + "'";
			contAcct = getGLPrefix(query, "BRANCH")
					+ getGLAccount(categoryId, acctType);
		}
		return contAcct;
	}

	public String processGLAccount(int categoryId, int code, String acctType) {
		String[] compInfo = getCompanyInfo();
		String sbuRequired = compInfo[4].trim();
		String sbuLevel = (compInfo[5]==null?"":compInfo[5].trim());
		String contAcct = "";
		String query = "";
		// Sector/Units
		if (sbuRequired.equalsIgnoreCase("Y")) {
			if (sbuLevel.equalsIgnoreCase("Department")) {
				query = "SELECT GL_PREFIX FROM AM_AD_DEPARTMENT WHERE DEPT_ID = "
						+ code;
				contAcct = getGLPrefix(query, "DEPARTMENT")
						+ getGLAccount(categoryId, acctType);
			}
			if (sbuLevel.equalsIgnoreCase("Sector/Units")) {
				query = "SELECT GL_PREFIX FROM AM_AD_SECTION WHERE SECTION_ID = "
						+ code;
				contAcct = getGLPrefix(query, "SECTION")
						+ getGLAccount(categoryId, acctType);
			}
		} else {
			query = "SELECT GL_PREFIX FROM AM_AD_BRANCH WHERE BRANCH_ID = "
					+ code;
			contAcct = getGLPrefix(query, "BRANCH")
					+ getGLAccount(categoryId, acctType);
		}
		return contAcct;
	}

	public String[] getLegacySystemDetail() {
		String SELECT_QUERY = "SELECT app_name,version,client_name,req_accttype,req_trancode,process_date FROM AM_AD_LEGACY_SYS_CONFIG ";
		String[] result = new String[6];

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(SELECT_QUERY);
			rs = ps.executeQuery();
			while (rs.next()) {
				result[0] = rs.getString("app_name");
				result[1] = rs.getString("version");
				result[2] = rs.getString("client_name");
				result[3] = rs.getString("req_accttype");
				result[4] = rs.getString("req_trancode");
				result[5] = formatDate(rs.getDate("process_date"));
			}
		} catch (Exception e) {
			e.getMessage();
		} finally {
			closeConnection(con, ps, rs);
		}
		return result;
	}

	// for generating batch ID for AM_ENTRY_TABLE
	public int getMaxNum(String userId) {
		int maxNum = 0;
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String qry = "SELECT MAX(TRANSACTION_ID) FROM AM_ENTRY_TABLE WHERE USER_ID = '"
				+ userId + "'";

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(qry);
			rs = ps.executeQuery();
			while (rs.next()) {
				maxNum = rs.getInt(1) + 1;
			}

		} catch (Exception e) {
			System.out
					.println("WARNING::ERROR FETCHING MAX FROM AM_ENTRY_TABLE "
							+ e);
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				System.out.println("WARNING::Error Closing Connection " + e);
			}
		}
		return maxNum;
	}

	private String getAccountCode(String code) {
		String result = "";
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String qry = "SELECT ACCT_TYPE FROM AM_AD_ACCOUNT_TYPE WHERE ACCT_SERIAL  = '"
				+ code + "'";

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(qry);
			rs = ps.executeQuery();
			while (rs.next()) {
				result = rs.getString(1);
			}

		} catch (Exception e) {
			String warning = "WARNING:Error fetching Acount Type ->"
					+ e.getMessage();
			System.out.println(warning);
			e.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return result;
	}

	// raise entry

	public int raiseEntry(String drAcct, String crAcct, String drAcctType,
			String crAcctType, String drTranCode, String crTranCode,
			String drNarration, String crNarration, double amount,
			String userId, String batchId, String entryDate, String superId) {
		int i = 0;
		// String superId = "0";
		String querylegacy = "SELECT Legacy_Sys_id FROM AM_GB_USER   "
				+ "WHERE USER_ID =" + userId;
		String legacyId = "";
		String tranCode = "";
		String postDate = entryDate; // dateConvert(new
										// java.util.Date()).toString();
		String effDate = dateConvert(new java.util.Date()).toString();
		String status = "U";
		String supervisor = "";
		String rejectReason = "";
		// String batchId = assetId;
		String legacySysDate = getLegacySystemDate();

		String query = "INSERT INTO am_entry_table(dr_acct,cr_acct,dr_narration,cr_narration,amount,user_id,"
				+ "super_id,legacy_id,dr_tran_code,cr_tran_code,posting_date,effective_date,process_status,supervisor,reject_reason,batch_id,dr_acct_type,cr_acct_type,tran_sent_time)"
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getConnection("legendPlus");
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery(querylegacy);
			rs.next();
			ps = con.prepareStatement(query);
			ps.setString(1, drAcct);
			ps.setString(2, crAcct);
			ps.setString(3, drNarration);
			ps.setString(4, crNarration);
			ps.setDouble(5, amount);
			ps.setString(6, userId);
			ps.setString(7, superId);
			ps.setString(8, rs.getString("Legacy_Sys_id"));
			ps.setString(9, drTranCode);
			ps.setString(10, crTranCode);
			ps.setDate(11, dateConvert(entryDate));
			ps.setDate(12, dateConvert(legacySysDate));
			ps.setString(13, status);
			ps.setString(14, supervisor);
			ps.setString(15, rejectReason);
			ps.setString(16, batchId);
			ps.setString(17, getAccountCode(drAcctType));
			ps.setString(18, getAccountCode(crAcctType));
			ps.setDate(19, dateConvert(effDate));
			i = ps.executeUpdate();

			// set status
		} catch (Exception e) {
			String warning = "WARNING:Error inserting Raise Entry ->"
					+ e.getMessage();
			System.out.println(warning);
			e.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return i;
	}

	public int raiseEntry(String drAcct, String crAcct, String drAcctType,
			String crAcctType, String drTranCode, String crTranCode,
			String drNarration, String crNarration, double amount,
			String userId, String batchId, String entryDate, String superId,
			String assetId, String tranType) {
		int i = 0;
		// String superId = "0";
		String legacyId = getLegacyID(Integer.parseInt(userId));
		String tranCode = "";
		//System.out.println("############the value of entryDate is "+ entryDate);
        String postDate = entryDate.substring(0, 10); // dateConvert(new
       // System.out.println("############the value of postDate is "+ postDate);							// java.util.Date()).toString();
		String effDate = dateConvert(new java.util.Date()).toString();
		String status = "U";
		String supervisor = "";
		String rejectReason = "";
		// String batchId = assetId;
		String legacySysDate = getLegacySystemDetail()[5].substring(0, 10);
         // System.out.println("@@@@@@@@@@the value of legacySysDate is "+ legacySysDate);
		String query = "INSERT INTO am_entry_table(dr_acct,cr_acct,dr_narration,cr_narration,amount,user_id,"
				+ "super_id,legacy_id,dr_tran_code,cr_tran_code,posting_date,effective_date,process_status,supervisor,"
				+ "reject_reason,batch_id,dr_acct_type,cr_acct_type,asset_ID,tran_type,tran_sent_time)"
				+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(query);
			ps.setString(1, drAcct);
			ps.setString(2, crAcct);
			ps.setString(3, drNarration);
			ps.setString(4, crNarration);
			ps.setDouble(5, amount);
			ps.setString(6, userId);
			ps.setString(7, superId);
			ps.setString(8, legacyId);
			ps.setString(9, drTranCode);
			ps.setString(10, crTranCode);
			ps.setDate(11, dateConvert(postDate));
			ps.setDate(12, dateConvert(legacySysDate));
			ps.setString(13, status);
			ps.setString(14, supervisor);
			ps.setString(15, rejectReason);
			ps.setString(16, batchId);
			ps.setString(17, getAccountCode(drAcctType));
			ps.setString(18, getAccountCode(crAcctType));
			ps.setString(19, assetId);
			ps.setString(20, tranType);
			ps.setDate(21,dateConvert(effDate));
			// ps.setDate(21,dateConvert(entryDate));
			i = ps.executeUpdate();

			// set status
		} catch (Exception e) {
			String warning = "WARNING:Error inserting Raise Entry ->"
					+ e.getMessage();
			System.out.println(warning);
			e.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return i;
	}

	public boolean isEntryRaised(String query) {
		boolean result = false;
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String x = "";
		// DataConnect connect = new DataConnect();
		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				x = rs.getString(1);
			}
			if (x.equalsIgnoreCase("R")) {
				result = true;
			}

		} catch (Exception e) {
			System.out.println("Error Retrieving " + query + e);
			// e.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return result;
	}

	public void setRaiseEntryStatus(String query) {
		excuteSQLCode(query);
	}

	private void excuteSQLCode(String sqlCode) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(sqlCode);
			ps.execute();
		} catch (Exception ex) {
			System.out.println("Error executing SQL Code ->\n" + sqlCode + "\n"
					+ ex);
		} finally {
			closeConnection(con, ps);
		}
	}

	public String getLegacySystemDate() {
		String query = "SELECT PROCESS_DATE FROM LEGACY_SYSTEM_CONFIG";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String result = "";
		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				result = formatDate(rs.getDate(1));
			}
		} catch (Exception e) {
			String warning = "WARNING:Error Fetching Legacy System Date"
					+ " ->" + e.getMessage();
			System.out.println(warning);
		} finally {
			closeConnection(con, ps, rs);
		}

		return result;
	}

	private String getLegacyID(int userId) {
		String result = "";
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String qry = "SELECT LEGACY_SYS_ID FROM AM_GB_USER WHERE USER_ID  = "
				+ userId;

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(qry);
			rs = ps.executeQuery();
			while (rs.next()) {
				result = rs.getString(1);
			}

		} catch (Exception e) {
			String warning = "WARNING:Error getUserLegacyID() ->"
					+ e.getMessage();
			System.out.println(warning);
			e.printStackTrace();
		} finally {
			closeConnection(con, ps, rs);
		}
		return result;
	}

	public void removeTransactionEntry(String id) {
		// if(!query.equalsIgnoreCase(""))
		if (isEntryExists(id)) {
			excuteSQLCode("DELETE FROM AM_ENTRY_TABLE WHERE ASSET_ID ='" + id
					+ "'");
		}

	}

	private boolean isEntryExists(String id) {
		boolean result = false;
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String query = "SELECT * FROM AM_ENTRY_TABLE WHERE ASSET_ID ='" + id
				+ "'";

		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				result = true;
			}

		} catch (Exception e) {
			System.out.println("Error isEntryExist(?) " + e);
			// e.printStackTrace();
		} finally {
			closeConnection(con, ps, rs);
		}
		return result;
	}

	public boolean isEntryRaise(String assetId) {
		boolean result = false;
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
//		String query = "SELECT {?} FROM {?} WHERE ASSET_ID = '"+assetId+"'";
		String query = "SELECT RAISE_ENTRY FROM AM_ASSET_DEP_ADJUSTMENT WHERE ASSET_ID = ?";
//		System.out.println("====query in isEntryRaise: "+query);
//		System.out.println("====parameter in isEntryRaise: "+tableName+"   selectField: "+selectField+"   assetId: "+assetId);
		PreparedStatement ps = null;
		String x = "";
		// DataConnect connect = new DataConnect();
		try {
			con = getConnection("legendPlus");
			ps = con.prepareStatement(query);
			ps.setString(1, assetId);
			rs = ps.executeQuery();
			while (rs.next()) {
				x = rs.getString(1);
			}
			if (x.equalsIgnoreCase("R")) {
				result = true;
			}
			//System.out.println("====X in isEntryRaise: "+x);

		} catch (Exception e) {
			System.out.println("Error Retrieving " + query + e);
			// e.printStackTrace();
		} finally {
			closeConnection(con, ps);
		}
		return result;
	}
	
}
