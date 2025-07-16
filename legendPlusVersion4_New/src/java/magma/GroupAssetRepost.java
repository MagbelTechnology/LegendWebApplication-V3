package magma;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import magma.net.dao.MagmaDBConnection;
import magma.util.Codes;

import com.magbel.util.DatetimeFormat;

public class GroupAssetRepost extends legend.ConnectionClass
{
		private String branch_id = "0";

		private String department_id = "0";
		
		private String section_id = "0";
		
		private String category_id = "0";
		
		private String make = "0";
		
		private String location = "0";
		
		private String maintained_by = "0";
		
		private String driver = "0";
		
		private String state = "0";
		
		private String supplied_by = "0";
		
		private String registration_no = "";
		
		private Calendar date_of_purchase = new GregorianCalendar();
		
		private String depreciation_start_date = "";
		
		private String depreciation_end_date = "";
		
		private String authorized_by = "";
		
		private String reason = "";
		
		private String no_of_items = "0";
		
		private String description = "";
		
		private String cost_price = "0";
		
		private String vatable_cost = "0";
		
		private String subject_to_vat = "N";
		
		private String vat_amount = "0";
		
		private String wh_tax_cb = "N";
		
		private String wh_tax_amount = "0";
		
		private String serial_number = "";
		
		private String engine_number = "";
		
		private String model = "";
		
		private String user = "";
		
		private String depreciation_rate = "";
		
		private String residual_value = getResidualvalue();
		
		private String require_depreciation = "";
		
		private String vendor_account = "";
		
		private String spare_1 = "";
		
		private String spare_2 = "";
		
		private String who_to_rem = "";
		
		private String email_1 = "";
		
		private String who_to_rem_2 = "";
		
		private String email2 = "";
		
		private String user_id = "";
		
		private Calendar posting_date = new GregorianCalendar();
		
		//private String posting_date = "";
		
		private String province = "";
		
		//private Calendar warrantyStartDate = new GregorianCalendar();
		
		private String warrantyStartDate = "";
		
		private String noOfMonths = "";
		
		private String raise_entry;
		
		private String status ="ACTIVE";
		
		private String supervisor ="";
		
		//private Calendar expiryDate = new GregorianCalendar();
		
		private String expiryDate = "";
		
		//added by ayojava to retrieve asset_id for insertion into am_group_asset
		private String asset_id = "";
		
		private String lpo="";
		
		private String  bar_code="";
		
		private String authuser="";
		
		private String require_redistribution = "";
		
		 private String group_id="";
		
		//added by ayojava
		private double accum_dep = 0.0d;
		
		java.text.SimpleDateFormat sdf;
		
		private DatetimeFormat dateFormat;
		
		private String amountPTD;
			
		private String amountREM;
		
		private String partPAY="N";
		
		private String fullyPAID="N";
		
		private String deferPay ="N";
		
		private String sbu_code ="";
		
		private String mtid="";
		
		private String who_to_remind="";
		
		private String section = "";
		
		private Codes code;

		public String getWho_to_remind() {
			return who_to_remind;
		}

		public void setWho_to_remind(String who_to_remind) {
			this.who_to_remind = who_to_remind;
		}

		public String getWho_to_remind_2() {
			return who_to_remind_2;
		}

		public void setWho_to_remind_2(String who_to_remind_2) {
			this.who_to_remind_2 = who_to_remind_2;
		}

		private String who_to_remind_2="";
		
        private  double asset_costPrice=0;
		
		private String update_am_group_asset_qry ="update am_group_asset set process_flag= ?,asset_id=?,REGISTRATION_NO=?, " + 	  
        "BRANCH_ID=?, DEPT_ID=?,"+ "SECTION_ID=?, CATEGORY_ID=?,DESCRIPTION=?, VENDOR_AC=?," +
        "DATE_PURCHASED=?, DEP_RATE=?, ASSET_MAKE=?, ASSET_MODEL=?,"+ 
        "ASSET_SERIAL_NO=?, ASSET_ENGINE_NO=?, SUPPLIER_NAME=?," +
        "ASSET_USER=?, ASSET_MAINTENANCE=?, ACCUM_DEP=?," +
        "COST_PRICE=?, DEP_END_DATE=?, RESIDUAL_VALUE=?," +
        "AUTHORIZED_BY=?, POSTING_DATE=?, EFFECTIVE_DATE=?, PURCHASE_REASON=?," +
        " LOCATION=?, " +
        "VATABLE_COST=?,VAT=?, WH_TAX=?, WH_TAX_AMOUNT=?, REQ_DEPRECIATION=?," +
        "REQ_REDISTRIBUTION=?, SUBJECT_TO_VAT=?, WHO_TO_REM=?, EMAIL1=?," +
        "WHO_TO_REM_2=?, EMAIL2=?, RAISE_ENTRY=?, DEP_YTD=?, SECTION=?," +
        "STATE=?, DRIVER=?, SPARE_1=?, SPARE_2=?, [USER_ID]=?," +
        "PROVINCE=?, WAR_START_DATE=?, WAR_MONTH=?, " +
        "WAR_EXPIRY_DATE=?,LPO=?,BAR_CODE=? ,BRANCH_CODE=?,CATEGORY_CODE=? ," +
        "GROUP_ID=?,PART_PAY=?,FULLY_PAID=?,DEFER_PAY=?,SBU_CODE=?"+
        "  where asset_id = ? ";
		                      

		private String update_am_asset_qry  =
		" update AM_ASSET set REGISTRATION_NO=?, BRANCH_ID=?, DEPT_ID=?," +
        "SECTION_ID=?, CATEGORY_ID=?, [DESCRIPTION]=?, VENDOR_AC=?," +
        "DATE_PURCHASED=?, DEP_RATE=?, ASSET_MAKE=?, ASSET_MODEL=?," +
        "ASSET_SERIAL_NO=?, ASSET_ENGINE_NO=?, SUPPLIER_NAME=?," +
        "ASSET_USER=?, ASSET_MAINTENANCE=?, ACCUM_DEP=?, MONTHLY_DEP=?," +
        "COST_PRICE=?, NBV=?, DEP_END_DATE=?, RESIDUAL_VALUE=?," +
        "AUTHORIZED_BY=?, POSTING_DATE=?, EFFECTIVE_DATE=?, PURCHASE_REASON=?," +
        "USEFUL_LIFE=?, TOTAL_LIFE=?, LOCATION=?, REMAINING_LIFE=?," +
        "VATABLE_COST=?,VAT=?, WH_TAX=?, WH_TAX_AMOUNT=?, REQ_DEPRECIATION=?," +
        "REQ_REDISTRIBUTION=?, SUBJECT_TO_VAT=?, WHO_TO_REM=?, EMAIL1=?," +
        "WHO_TO_REM_2=?, EMAIL2=?, RAISE_ENTRY=?, DEP_YTD=?, [SECTION]=?," +
        "STATE=?, DRIVER=?, SPARE_1=?, SPARE_2=?, ASSET_STATUS=?, [USER_ID]=?," +
        "MULTIPLE=?,PROVINCE=?, WAR_START_DATE=?, WAR_MONTH=?, " +
        "WAR_EXPIRY_DATE=?,LPO=?,BAR_CODE=? ,BRANCH_CODE=?,CATEGORY_CODE=? ," +
        "GROUP_ID=?,PART_PAY=?,FULLY_PAID=?,DEFER_PAY=?,SBU_CODE=?,SECTION_CODE=? " +
        "where ASSET_ID = ";
		
		
		private MagmaDBConnection dbConnection;

	public GroupAssetRepost() throws Exception
	{
		super();
		sdf = new SimpleDateFormat("dd-MM-yyyy");
		dbConnection = new MagmaDBConnection();
		dateFormat = new DatetimeFormat();
		code = new Codes();
	}

	public long [] updateGroupAssetRecord(String gid) throws Exception, Throwable 
	{
		String[] budget = getBudgetInfo();
		double[] bugdetvalues = getBudgetValues();
		int DONE = 0; // everything is oK
		int BUDGETENFORCED = 1; // EF budget = Yes, CF = NO, ERROR_FLAG
		int BUDGETENFORCEDCF = 2; // EF budget = Yes, CF = Yes, ERROR_FLAG
		int ASSETPURCHASEBD = 3; // asset falls into no quarter purchase date
		long [] value = new long[2];
    	String Q = getQuarter();
		if(budget[3].equalsIgnoreCase("N"))
		{
			value[1]=updateCreatedGroup(gid);
			value[0]= DONE;
		}
		else if(budget[3].equalsIgnoreCase("Y"))
		{
		if (!Q.equalsIgnoreCase("NQ")) 
		{
			if (budget[3].equalsIgnoreCase("Y")	&& budget[4].equalsIgnoreCase("N"))
			{
				if (chkBudgetAllocation(Q, bugdetvalues, false))
				{
					updateBudget(Q, budget);
					value[1]=updateCreatedGroup(gid);
					value[0]= DONE;
				} 
				else
				{
					value[0]=  BUDGETENFORCED;
				}
			} 
			else if (budget[3].equalsIgnoreCase("Y")&& budget[4].equalsIgnoreCase("Y"))
			{
				if (chkBudgetAllocation(Q, bugdetvalues, true)) 
				{
					updateBudget(Q, budget);
					value[1]=updateCreatedGroup(gid);
					value[0]=  DONE;
				}
				else
				{
					value[0]=  BUDGETENFORCEDCF;
				}
			} 
			else 
			{
				value[1]=updateCreatedGroup(gid);
				value[0]= DONE;
			}
		} 
		else 
		{
			value[0]= ASSETPURCHASEBD;
		}
	}
	else
		{
			
		}
		return value;
	}
	
	public String[] getBudgetInfo() {
		String[] result = new String[5];

		String query = " SELECT financial_start_date,financial_no_ofmonths"
				+ ",financial_end_date,enforce_acq_budget,quarterly_surplus_cf"
				+ " FROM am_gb_company";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = dbConnection.getConnection("fixedasset");
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				result[0] = sdf.format(rs.getDate("financial_start_date"));
				result[1] = rs.getString("financial_no_ofmonths");
				result[2] = sdf.format(rs.getDate("financial_end_date"));
				result[3] = rs.getString("enforce_acq_budget");
				result[4] = rs.getString("quarterly_surplus_cf");

			}
		} catch (Exception e) {
			String warning = "WARNING:Error Fetching Company Details" + " ->"
					+ e.getMessage();
			System.out.println(warning);
		} finally {
			dbConnection.closeConnection(con, ps, rs);
		}

		return result;
	}

	public double[] getBudgetValues() {
		

		
		double[] result = new double[8];

		String query = " SELECT [Q1_ALLOCATION],[Q1_ACTUAL],[Q2_ALLOCATION]"
				+ ",[Q2_ACTUAL],[Q3_ALLOCATION],[Q3_ACTUAL],[Q4_ALLOCATION],[Q4_ACTUAL]"
				+ " FROM [AM_ACQUISITION_BUDGET] WHERE [CATEGORY]='"
				+ getCatCode() + "' AND " + " [BRANCH_ID]='" + branch_id + "'";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = dbConnection.getConnection("fixedasset");
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				result[0] = rs.getDouble("Q1_ALLOCATION");
				result[1] = rs.getDouble("Q1_ACTUAL");
				result[2] = rs.getDouble("Q2_ALLOCATION");
				result[3] = rs.getDouble("Q2_ACTUAL");
				result[4] = rs.getDouble("Q3_ALLOCATION");
				result[5] = rs.getDouble("Q3_ACTUAL");
				result[6] = rs.getDouble("Q4_ALLOCATION");
				result[7] = rs.getDouble("Q4_ACTUAL");
				// result[4] = rs.getDouble("quarterly_surplus_cf");

			}
		} catch (Exception e) {
			String warning = "WARNING:Error Fetching Company Details" + " ->"
					+ e.getMessage();
			System.out.println(warning);
		} finally {
			dbConnection.closeConnection(con, ps, rs);
		}

		return result;
	}

	public String getQuarter() {
		//System.out.println("GETTING QUARTERS!!!!");
		String quarter = "NQ";
		String[] budg = getBudgetInfo();
		//System.out.println("fsdate  " + budg[0]);
		//System.out.println("pdate  " + date_of_purchase);
		double q1 = (double) (Double.parseDouble(budg[1]) / 4);
		int month = (int) dateFormat.getDayDifference(sdf
				.format(date_of_purchase.getTime()), budg[0]) / 30;
		//System.out.println("pdate  " + date_of_purchase);
		//System.out.println("financial start and pdate diff months " + month);
		boolean btw = dateFormat.isDateBetween(budg[0], budg[2], sdf
				.format(date_of_purchase.getTime()));
		//System.out.println("btw : " + btw);
		if (btw) {
			if ((double) month <= q1) {
				quarter = "FIRST";
				//System.out.println("1ST QUARTER");
			} else if ((double) month > q1 && (double) month <= (q1 * 2.0)) {
				quarter = "2ND";
				//System.out.println("2ND QUARTER");
			} else if ((double) month > (q1 * 2.0)
					&& (double) month <= (q1 * 3.0)) {
				quarter = "3RD";
				//System.out.println("3RD QUARTER");
			} else if (month > (q1 * 3.0)) {
				quarter = "4TH";
				//System.out.println("4TH QUARTER");
			}

		}
		//System.out.println("the assets quarter is  " + quarter);
		return quarter;

	}

	public String getCatCode() {

		String query = "SELECT CATEGORY_CODE  FROM am_ad_category  "
				+ "WHERE category_id = '" + category_id + "' ";
		String catid = "0";
		try {

			ResultSet rs = getStatement().executeQuery(query);
			while (rs.next()) {

				catid = rs.getString(1);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			freeResource();
		}

		return catid;

	}
	
	public boolean chkBudgetAllocation(String quarter, double values[],
			boolean cf) {
		boolean allocation = true;
		double result = 0.00;
		if (cf) {
			if (quarter.equalsIgnoreCase("FIRST")) {
				result = values[0]
						- (values[1] + Double.parseDouble(vatable_cost
								.replaceAll(",", "")));
			} else if (quarter.equalsIgnoreCase("2ND")) {
				result = (values[0] + values[2])
						- (values[1] + values[3] + Double
								.parseDouble(vatable_cost.replaceAll(",", "")));
			} else if (quarter.equalsIgnoreCase("3RD")) {
				result = (values[0] + values[2] + values[4])
						- (values[1] + values[3] + values[5] + Double
								.parseDouble(vatable_cost.replaceAll(",", "")));
			} else if (quarter.equalsIgnoreCase("4TH")) {
				result = (values[0] + values[2] + values[4] + values[6])
						- (values[1] + values[3] + values[5] + values[7] + Double
								.parseDouble(vatable_cost.replaceAll(",", "")));
			}

		} else {
			if (quarter.equalsIgnoreCase("FIRST")) {
				result = values[0]
						- (values[1] + Double.parseDouble(vatable_cost
								.replaceAll(",", "")));
			} else if (quarter.equalsIgnoreCase("2ND")) {
				result = values[2]
						- (values[3] + Double.parseDouble(vatable_cost
								.replaceAll(",", "")));
			} else if (quarter.equalsIgnoreCase("3RD")) {
				result = values[4]
						- (values[5] + Double.parseDouble(vatable_cost
								.replaceAll(",", "")));
			} else if (quarter.equalsIgnoreCase("4TH")) {
				result = values[6]
						- (values[7] + Double.parseDouble(vatable_cost
								.replaceAll(",", "")));
			}

		}

		if (result < 0) {
			allocation = false;
		}
		return allocation;
	}
	
	public void updateBudget(String quarter, String[] bugdetinfo) {

		String fisdate = "";
		int finomonth = 0;
		String fiedate = "";
		Connection conn = dbConnection.getConnection("fixedasset");
		;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			//System.out.println("pdate  " + date_of_purchase);
			//System.out
			//		.println("Commencing update of Aquicisition Budget due to Asset Creation");
			//System.out.println(category_id);
			String old_category = getCatCode();
			if (quarter.equalsIgnoreCase("FIRST")) {
				String budgetUpdate1 = "UPDATE AM_ACQUISITION_BUDGET "
						+ " SET Q1_ACTUAL = (Q1_ACTUAL + "
						+ vatable_cost.replaceAll(",", "")
						+ ") WHERE BRANCH_ID='" + branch_id
						+ "' AND CATEGORY='" + old_category
						+ "' AND ACC_START_DATE='"
						+ dateFormat.dateConvert(bugdetinfo[0])
						+ "' AND ACC_END_DATE='"
						+ dateFormat.dateConvert(bugdetinfo[2]) + "'";
				///System.out.println(budgetUpdate1);
				stmt.executeUpdate(budgetUpdate1);
				//System.out.println("Updated 1st Quarter");
			} else if (quarter.equalsIgnoreCase("2ND")) {
				String budgetUpdate1 = "UPDATE AM_ACQUISITION_BUDGET "
						+ " SET Q2_ACTUAL = (Q2_ACTUAL + "
						+ vatable_cost.replaceAll(",", "")
						+ ") WHERE BRANCH_ID='" + branch_id
						+ "' AND CATEGORY='" + old_category
						+ "' AND ACC_START_DATE='"
						+ dateFormat.dateConvert(bugdetinfo[0])
						+ "' AND ACC_END_DATE='"
						+ dateFormat.dateConvert(bugdetinfo[2]) + "'";

				//System.out.println(budgetUpdate1);

				stmt.executeUpdate(budgetUpdate1);
				//System.out.println("Updated 2nd Quarter");
			} else if (quarter.equalsIgnoreCase("3RD")) {
				String budgetUpdate1 = "UPDATE AM_ACQUISITION_BUDGET "
						+ " SET Q3_ACTUAL =(Q3_ACTUAL + "
						+ vatable_cost.replaceAll(",", "")
						+ ") WHERE BRANCH_ID='" + branch_id
						+ "' AND CATEGORY='" + old_category
						+ "' AND ACC_START_DATE='"
						+ dateFormat.dateConvert(bugdetinfo[0])
						+ "' AND ACC_END_DATE='"
						+ dateFormat.dateConvert(bugdetinfo[2]) + "'";

				stmt.executeUpdate(budgetUpdate1);

				} else if (quarter.equalsIgnoreCase("4TH")) {
				String budgetUpdate1 = "UPDATE AM_ACQUISITION_BUDGET "
						+ " SET Q4_ACTUAL = (Q4_ACTUAL + "
						+ vatable_cost.replaceAll(",", "")
						+ ") WHERE BRANCH_ID='" + branch_id
						+ "' AND CATEGORY='" + old_category
						+ "' AND ACC_START_DATE='"
						+ dateFormat.dateConvert(bugdetinfo[0])
						+ "' AND ACC_END_DATE='"
						+ dateFormat.dateConvert(bugdetinfo[2]) + "'";

				//System.out.println(budgetUpdate1);

				stmt.executeUpdate(budgetUpdate1);

				//System.out.println("Updated 4th Quarter");
			}

		} catch (Exception ex) {
			System.out.println("ERROR_ " + this.getClass().getName() + "---"
					+ ex.getMessage() + "--");
			ex.printStackTrace();
		} finally {
			// freeResource();
			dbConnection.closeConnection(conn, stmt);
		}
		System.out
				.println("Exiting update of Aquicisition Budget due to Reclassification");
	}
	public String getResidualvalue() throws Exception {

		String selectQuery = "SELECT RESIDUAL_VALUE FROM AM_GB_COMPANY";
		String residualValue = "0.00";
		try {

			ResultSet rs = getStatement().executeQuery(selectQuery);
			rs.next();
			residualValue = rs.getString(1);

		} catch (Exception e) {
			System.out.println("INFO: Error getting residualValue >>" + e);
		} finally {
			freeResource();
		}

		return residualValue;

	}
	
	public String getBranch_id() {
		return branch_id;
	}

	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
	}

	public String getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(String department_id) {
		this.department_id = department_id;
	}

	public String getSection_id() {
		return section_id;
	}

	public void setSection_id(String section_id) {
		this.section_id = section_id;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getMaintained_by() {
		return maintained_by;
	}

	public void setMaintained_by(String maintained_by) {
		this.maintained_by = maintained_by;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSupplied_by() {
		return supplied_by;
	}

	public void setSupplied_by(String supplied_by) {
		this.supplied_by = supplied_by;
	}

	public String getRegistration_no() {
		return registration_no;
	}

	public void setRegistration_no(String registration_no) {
		this.registration_no = registration_no;
	}

	public Calendar getDate_of_purchase() {
		return date_of_purchase;
	}

	public void setDate_of_purchase(Calendar date_of_purchase) {
		this.date_of_purchase = date_of_purchase;
	}

	public String getDepreciation_start_date() {
		return depreciation_start_date;
	}

	public void setDepreciation_start_date(String depreciation_start_date) {
		this.depreciation_start_date = depreciation_start_date;
	}

	public String getDepreciation_end_date() {
		return depreciation_end_date;
	}

	public void setDepreciation_end_date(String depreciation_end_date) {
		this.depreciation_end_date = depreciation_end_date;
	}

	public String getAuthorized_by() {
		return authorized_by;
	}

	public void setAuthorized_by(String authorized_by) {
		this.authorized_by = authorized_by;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getNo_of_items() {
		return no_of_items;
	}

	public void setNo_of_items(String no_of_items) {
		this.no_of_items = no_of_items;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCost_price() {
		return cost_price;
	}

	public void setCost_price(String cost_price) {
		this.cost_price = cost_price;
	}

	public String getVatable_cost() {
		return vatable_cost;
	}

	public void setVatable_cost(String vatable_cost) {
		this.vatable_cost = vatable_cost;
	}

	public String getSubject_to_vat() {
		return subject_to_vat;
	}

	public void setSubject_to_vat(String subject_to_vat) {
		this.subject_to_vat = subject_to_vat;
	}

	public String getVat_amount() {
		return vat_amount;
	}

	public void setVat_amount(String vat_amount) {
		this.vat_amount = vat_amount;
	}

	public String getWh_tax_cb() {
		return wh_tax_cb;
	}

	public void setWh_tax_cb(String wh_tax_cb) {
		this.wh_tax_cb = wh_tax_cb;
	}

	public String getWh_tax_amount() {
		return wh_tax_amount;
	}

	public void setWh_tax_amount(String wh_tax_amount) {
		this.wh_tax_amount = wh_tax_amount;
	}

	public String getSerial_number() {
		return serial_number;
	}

	public void setSerial_number(String serial_number) {
		this.serial_number = serial_number;
	}

	public String getEngine_number() {
		return engine_number;
	}

	public void setEngine_number(String engine_number) {
		this.engine_number = engine_number;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDepreciation_rate() {
		return depreciation_rate;
	}

	public void setDepreciation_rate(String depreciation_rate) {
		this.depreciation_rate = depreciation_rate;
	}

	public String getResidual_value() {
		return residual_value;
	}

	public void setResidual_value(String residual_value) {
		this.residual_value = residual_value;
	}

	public String getRequire_depreciation() {
		return require_depreciation;
	}

	public void setRequire_depreciation(String require_depreciation) {
		this.require_depreciation = require_depreciation;
	}

	public String getVendor_account() {
		return vendor_account;
	}

	public void setVendor_account(String vendor_account) {
		this.vendor_account = vendor_account;
	}

	public String getSpare_1() {
		return spare_1;
	}

	public void setSpare_1(String spare_1) {
		this.spare_1 = spare_1;
	}

	public String getSpare_2() {
		return spare_2;
	}

	public void setSpare_2(String spare_2) {
		this.spare_2 = spare_2;
	}

	public String getWho_to_rem() {
		return who_to_rem;
	}

	public void setWho_to_rem(String who_to_rem) {
		this.who_to_rem = who_to_rem;
	}

	public String getEmail_1() {
		return email_1;
	}

	public void setEmail_1(String email_1) {
		this.email_1 = email_1;
	}

	public String getWho_to_rem_2() {
		return who_to_rem_2;
	}

	public void setWho_to_rem_2(String who_to_rem_2) {
		this.who_to_rem_2 = who_to_rem_2;
	}

	public String getEmail2() {
		return email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public Calendar getPosting_date() {
		return posting_date;
	}

	public void setPosting_date(Calendar posting_date) {
		this.posting_date = posting_date;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getWarrantyStartDate() {
		return warrantyStartDate;
	}

	public void setWarrantyStartDate(String warrantyStartDate) {
		this.warrantyStartDate = warrantyStartDate;
	}

	public String getNoOfMonths() {
		return noOfMonths;
	}

	public void setNoOfMonths(String noOfMonths) {
		this.noOfMonths = noOfMonths;
	}

	public String getRaise_entry() {
		return raise_entry;
	}

	public void setRaise_entry(String raise_entry) {
		this.raise_entry = raise_entry;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getAsset_id() {
		return asset_id;
	}

	public void setAsset_id(String asset_id) {
		this.asset_id = asset_id;
	}

	public String getLpo() {
		return lpo;
	}

	public void setLpo(String lpo) {
		this.lpo = lpo;
	}

	public String getBar_code() {
		return bar_code;
	}

	public void setBar_code(String bar_code) {
		this.bar_code = bar_code;
	}

	public String getAuthuser() {
		return authuser;
	}

	public void setAuthuser(String authuser) {
		this.authuser = authuser;
	}

	public String getRequire_redistribution() {
		return require_redistribution;
	}

	public void setRequire_redistribution(String require_redistribution) {
		this.require_redistribution = require_redistribution;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public double getAccum_dep() {
		return accum_dep;
	}

	public void setAccum_dep(double accum_dep) {
		this.accum_dep = accum_dep;
	}

	public DatetimeFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(DatetimeFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getAmountPTD() {
		return amountPTD;
	}

	public void setAmountPTD(String amountPTD) {
		this.amountPTD = amountPTD;
	}

	public String getAmountREM() {
		return amountREM;
	}

	public void setAmountREM(String amountREM) {
		this.amountREM = amountREM;
	}

	public String getPartPAY() {
		return partPAY;
	}

	public void setPartPAY(String partPAY) {
		this.partPAY = partPAY;
	}

	public String getFullyPAID() {
		return fullyPAID;
	}

	public void setFullyPAID(String fullyPAID) {
		this.fullyPAID = fullyPAID;
	}

	public String getDeferPay() {
		return deferPay;
	}

	public void setDeferPay(String deferPay) {
		this.deferPay = deferPay;
	}

	public String getSbu_code() {
		return sbu_code;
	}

	public void setSbu_code(String sbu_code) {
		this.sbu_code = sbu_code;
	}

	public String getMtid() {
		return mtid;
	}

	public void setMtid(String mtid) {
		this.mtid = mtid;
	}

	public MagmaDBConnection getDbConnection() {
		return dbConnection;
	}

	public void setDbConnection(MagmaDBConnection dbConnection) {
		this.dbConnection = dbConnection;
	}

	private String update_am_group_asset_main_qry = 
		"UPDATE am_group_asset_main  SET [quantity] = ?,[Registration_No] = ?,[Branch_ID] = ? "+
		                  ",[Dept_ID] = ?,[Category_ID] = ?,[section_id] = ?,[Description] = ? ,[Vendor_AC] =? " + 
		                  ",[Date_purchased] = ?,[dep_rate] = ?,[asset_make] = ?,[asset_model] = ?,[asset_serial_no] =? " +
		                  ",[asset_engine_no] = ?,[supplier_name] = ?,[asset_user] = ?,[asset_maintenance] = ? "+
		                  ",[Cost_Price] = ?,[dep_end_date] = ?,[residual_value] = ?,[authorized_by] = ?,[wh_tax] = ? " +
		                  ",[wh_tax_amount] = ?,[req_redistribution] = ?,[Posting_Date] = ?,[effective_date] = ?"+
		                  ",[purchase_reason] = ?,[location] = ?,[Vatable_Cost] =?,[Vat] = ?,[Req_Depreciation] = ?"+
		                  ",[Subject_TO_Vat] = ?,[Who_TO_Rem] = ?,[email1] = ?,[who_to_rem_2] = ?,[email2] =?"+
		                  ",[dep_ytd] = ?,[Section] = ?,[Asset_Status] = ?,[state] = ?,[driver] = ?"+
		                  ",[spare_1] =?,[spare_2] = ?,[user_ID] = ?,[province] =?,[WAR_START_DATE] =?,[WAR_MONTH] = ?"+
		                  ",[WAR_EXPIRY_DATE] = ?,[branch_code] = ?,[dept_code] = ?,[section_code] = ?,[category_code] = ?"+
		                  ",[AMOUNT_PTD] =?,[AMOUNT_REM] = ?,[PART_PAY] =?,[FULLY_PAID] = ?,[supervisor] = ?,[BAR_CODE] =?"+
		                  ",[LPO] =?,[ACCUM_DEP] = ?,[defer_pay] = ?,[process_flag] = ?,[SBU_CODE] =?"+
		                  ",[pend_GrpAssets] =? WHERE group_id = ";
	
	private long updateCreatedGroup(String gid) 
	{
		int itemsCount = Integer.parseInt(no_of_items);
		
		Connection con = null;
        PreparedStatement ps = null;
        try
	    {
        	
        	con = dbConnection.getConnection("fixedasset");
            ps = con.prepareStatement(update_am_group_asset_main_qry);
            ps.setInt(1, itemsCount);
            ps.setString(2, registration_no);
            ps.setInt(3, Integer.parseInt(branch_id));
            ps.setInt(4, Integer.parseInt(department_id));
            ps.setInt(5, Integer.parseInt(category_id));
            ps.setInt(6, Integer.parseInt(section_id));
            ps.setString(7, description);
            ps.setString(8, vendor_account);
            ps.setString(9, DateManipulations.CalendarToDb(date_of_purchase));
            ps.setString(10, getDepreciationRate(category_id));
            ps.setString(11, make);
            ps.setString(12, model);
            ps.setString(13, serial_number);
            ps.setString(14, engine_number);
            ps.setInt(15, Integer.parseInt(supplied_by));
            ps.setString(16, user);
            ps.setInt(17, Integer.parseInt(maintained_by));
            ps.setDouble(18, Double.parseDouble(cost_price));
            ps.setDate(19, dbConnection.dateConvert(depreciation_end_date));
            ps.setDouble(20, Double.parseDouble(residual_value));
            ps.setString(21, authorized_by);
            ps.setString(22, wh_tax_cb);
            ps.setDouble(23, Double.parseDouble(wh_tax_amount));
            ps.setString(24, require_redistribution);
            ps.setString(25,DateManipulations.CalendarToDb(posting_date));
            ps.setDate(26, dbConnection.dateConvert(depreciation_start_date));
            ps.setString(27, reason);
            ps.setInt(28, Integer.parseInt(location));
            ps.setDouble(29, Double.parseDouble(vatable_cost));
            ps.setDouble(30, Double.parseDouble(vat_amount));
            ps.setString(31, require_depreciation);
            ps.setString(32, subject_to_vat);
            ps.setString(33, who_to_remind);
            ps.setString(34, email_1);
            ps.setString(35, who_to_remind_2);
            ps.setString(36, email2);
            ps.setString(37, "0");
            ps.setString(38, section);
            ps.setString(39, status);
            ps.setInt(40, Integer.parseInt(state));
            ps.setInt(41, Integer.parseInt(driver));
            ps.setString(42, spare_1);
            ps.setString(43, spare_2);
            ps.setString(44, user_id);
            ps.setString(45, province);
            ps.setDate(46, dbConnection.dateConvert(warrantyStartDate));
            ps.setInt(47, Integer.parseInt(noOfMonths));
            ps.setDate(48, dbConnection.dateConvert(expiryDate));
            ps.setString(49,code.getBranchCode(branch_id));
            ps.setString(50,code.getDeptCode(department_id));
            ps.setString(51,code.getSectionCode(section_id));
            ps.setString(52,code.getCategoryCode(category_id));
            ps.setDouble(53, Double.parseDouble(amountPTD));
            ps.setDouble(54, (Double.parseDouble(cost_price))-(Double.parseDouble(amountPTD)));
            ps.setString(55, partPAY);
            ps.setString(56, fullyPAID);
            ps.setString(57,supervisor);
            ps.setString(58,bar_code);
            ps.setString(59,lpo);
            ps.setString(60,deferPay);
            ps.setDouble(61, accum_dep);
            ps.setString(62,"N");
            ps.setString(63, sbu_code);
            ps.setInt(64, itemsCount);
            ps.setString(65, group_id);
            
            ps.executeUpdate();
            
            updateCreatedGroupAsset();
            
	    }
        catch (Exception ex) 
        {
	           System.out.println("WARNING: update am_group_asset_main in GroupAssetRepost: "+ex.getMessage());
	    } 
        finally 
        {
	    	   dbConnection.closeConnection(con, ps);
	    }
		return Long.parseLong(group_id);
	}
	
	String update_created_asset_qry ="update am_group_asset set process_flag= ?,REGISTRATION_NO=?, " + 	  
    "BRANCH_ID=?, DEPT_ID=?,SECTION_ID=?, CATEGORY_ID=?,DESCRIPTION=?, VENDOR_AC=?," +
    "DATE_PURCHASED=?, DEP_RATE=?, ASSET_MAKE=?, ASSET_MODEL=?,"+ 
    "ASSET_SERIAL_NO=?, ASSET_ENGINE_NO=?, SUPPLIER_NAME=?," +
    "ASSET_USER=?, ASSET_MAINTENANCE=?, ACCUM_DEP=?," +
    "COST_PRICE=?, DEP_END_DATE=?, RESIDUAL_VALUE=?," +
    "AUTHORIZED_BY=?, POSTING_DATE=?, EFFECTIVE_DATE=?, PURCHASE_REASON=?," +
    " LOCATION=?, " +
    "VATABLE_COST=?,VAT=?, WH_TAX=?, WH_TAX_AMOUNT=?, REQ_DEPRECIATION=?," +
    "REQ_REDISTRIBUTION=?, SUBJECT_TO_VAT=?, WHO_TO_REM=?, EMAIL1=?," +
    "WHO_TO_REM_2=?, EMAIL2=?, RAISE_ENTRY=?, DEP_YTD=?, SECTION=?," +
    "STATE=?, DRIVER=?, SPARE_1=?, SPARE_2=?, [USER_ID]=?," +
    "PROVINCE=?, WAR_START_DATE=?, WAR_MONTH=?, " +
    "WAR_EXPIRY_DATE=?,LPO=?,BAR_CODE=? ,BRANCH_CODE=?,CATEGORY_CODE=? ,[dept_code] = ?,[section_code] = ?" +
    "PART_PAY=?,FULLY_PAID=?,DEFER_PAY=?,SBU_CODE=?,[Asset_Status] = ? "+
    "  where group_id = ? " ;	
	
	private void  updateCreatedGroupAsset()
	{
		int itemsCount = Integer.parseInt(no_of_items);
		Connection con = null;
        PreparedStatement ps = null;
		con = dbConnection.getConnection("fixedasset");
		
			try
			{
				ps = con.prepareStatement(update_created_asset_qry);
				ps.setString(1, "N");
	            ps.setString(2, registration_no);
	            ps.setInt(4, Integer.parseInt(branch_id));
	            ps.setInt(5, Integer.parseInt(department_id));
	            ps.setInt(6, Integer.parseInt(section_id));
	            ps.setInt(7, Integer.parseInt(category_id));
	            ps.setString(8, description);
	            ps.setString(9, vendor_account);
	            ps.setString(10, DateManipulations.CalendarToDb(date_of_purchase));
	            ps.setString(11, getDepreciationRate(category_id));
	            ps.setString(12, make);
	            ps.setString(13, model);
	            ps.setString(14, serial_number);
	            ps.setString(15, engine_number);
	            ps.setInt(16, Integer.parseInt(supplied_by));
	            ps.setString(17, user);
	            ps.setInt(18, Integer.parseInt(maintained_by));
	            ps.setInt(19, 0);
	            ps.setDouble(20,  Double.parseDouble(cost_price) / itemsCount);
	            ps.setDate(21, dbConnection.dateConvert(depreciation_end_date));
	            ps.setDouble(22, Double.parseDouble(residual_value));
	            ps.setString(23, authorized_by);
	            ps.setDate(24, dbConnection.dateConvert(new java.util.Date()));
	            ps.setDate(25, dbConnection.dateConvert(depreciation_start_date));
	            ps.setString(26, reason);
	            ps.setInt(27, Integer.parseInt(location));
	            ps.setDouble(28, Double.parseDouble(vatable_cost));
	            ps.setDouble(29, Double.parseDouble(vat_amount));
	            ps.setString(30, wh_tax_cb);
	            ps.setDouble(31, Double.parseDouble(wh_tax_amount));
	            ps.setString(32, require_depreciation);
	            ps.setString(33, require_redistribution);
	            ps.setString(34, subject_to_vat);
	            ps.setString(35, who_to_remind);
	            ps.setString(36, email_1);
	            ps.setString(37, who_to_remind_2);
	            ps.setString(38, email2);
	            ps.setString(39, "N");
	            ps.setString(40, "0");
	            ps.setString(41, section);
	            ps.setInt(42, Integer.parseInt(state));
	            ps.setInt(43, Integer.parseInt(driver));
	            ps.setString(44, spare_1);
	            ps.setString(45, spare_2);
	            ps.setString(46, user_id);
	            ps.setString(47, province);
	            ps.setDate(48, dbConnection.dateConvert(warrantyStartDate));
	            ps.setInt(49, Integer.parseInt(noOfMonths));
	            ps.setDate(50, dbConnection.dateConvert(expiryDate));
	            ps.setString(51,lpo);
	            ps.setString(52,bar_code);
	            ps.setString(53,code.getBranchCode(branch_id));
	            ps.setString(54,code.getCategoryCode(category_id));
	            ps.setString(60,code.getDeptCode(department_id));
	            ps.setString(61,code.getSectionCode(section_id));
	            ps.setString(56, partPAY);
	            ps.setString(57, fullyPAID);
	            ps.setString(58, deferPay);
	            ps.setString(59,sbu_code);
	            ps.setString(62,"ACTIVE");
	             ps.setString(62, group_id);
	            int result = ps.executeUpdate();
			}
			catch (Exception ex)
			{
				System.out.println("WARNING: update am_group_asset in GroupAssetRepost: " + ex.getMessage());
			}
			finally 
			{
				 dbConnection.closeConnection(con, ps);
			}
	}
	
	
	
	private String getDepreciationRate(String category_id) throws Exception {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String rate = "0.0";
        String query = "SELECT DEP_RATE FROM AM_AD_CATEGORY " +
                       "WHERE CATEGORY_ID = " + category_id;
        try {
            con = dbConnection.getConnection("fixedasset");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                rate = rs.getString(1);
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error fetching DepreciationRate ->" + ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return rate;
    }
}
