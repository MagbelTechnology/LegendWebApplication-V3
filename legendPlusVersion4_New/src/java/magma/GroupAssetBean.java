package magma;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.legend.vao.SendMail;
import com.magbel.legend.vao.ViewAssetDetails;
import com.magbel.util.ApplicationHelper;
import com.magbel.util.DatetimeFormat;
import com.magbel.util.HtmlUtility;

import legend.admin.handlers.CompanyHandler;
import magma.net.dao.MagmaDBConnection;
import magma.net.manager.FleetHistoryManager;
import magma.net.vao.Asset;
import magma.util.Codes;
import magma.AssetReclassificationBean;

@SuppressWarnings("unused")
public class GroupAssetBean extends legend.ConnectionClass {
	private String branch_id = "0";

	private String department_id = "0";

	private String section_id = "0";

	private String category_id = "0";
	
	private String integrifyId = "";

	private String sub_category_id = "0";
	
	private String make = "0";

	private String location = "0";

	private String maintained_by = "0";

	private String driver = "0";

	private String section = "";

	private String costPrice_flag="";

    private String invoiceNum="";
    private String projectCode="";
    private String workstationIp;

    private String workstationName;

    private String mac_address;


    ApprovalRecords aprecords = null;
//        AssetRecordsBean ad = null;
    CompanyHandler comp = null;
        
    public String getMac_address() {
        return mac_address;
    }

    public void setMac_address(String mac_address) {
        this.mac_address = mac_address;
    }

    public String getWorkstationIp() {
        return workstationIp;
    }
    
    HtmlUtility htmlUtil;
    
    public void setWorkstationIp(String workstationIp) {
        this.workstationIp = workstationIp;
    }

    public String getWorkstationName() {
        return workstationName;
    }

    public void setWorkstationName(String workstationName) {
        this.workstationName = workstationName;
    }

	public String getCostPrice_flag()
	{
		if ((costPrice_flag== null)||(costPrice_flag.equals("")))
		{
			costPrice_flag="N";
		}
		return costPrice_flag;
	}

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

	public void setCostPrice_flag(String costPrice_flag) {
		this.costPrice_flag = costPrice_flag;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	private String state = "0";

	private String supplied_by = "0";

	private String registration_no = "";

	private Calendar date_of_purchase = new GregorianCalendar();

	//private String date_of_purchase = "";

//	private Calendar depreciation_start_date = new GregorianCalendar();
//
//	private Calendar depreciation_end_date = new GregorianCalendar();

	private String depreciation_start_date = "";
    private String depreciation_end_date = "";

	private String authorized_by = "";

	private String reason = "";

	private String no_of_items = "0";

	private String description = "";

	private String cost_price = "0";

	private String transport_cost = "0";
	
	private String other_cost = "0";
	
	private String vatable_cost = "0";

	private String subject_to_vat = "N";

	private String vat_amount = "0";

	private String wh_tax_cb = "N";

	private String wh_tax_amount = "0";

	private String serial_number = "";

	private String engine_number = "";

	private String model = "";

	private String user = "";

	private String depreciation_rate = "100";

	private String residual_value = getResidualvalue();

	private String require_depreciation = "N";

	private String vendor_account = "";

	private String spare_1 = "";

	private String spare_2 = "";

	private String spare_3 = "";

	private String spare_4 = "";

	private String spare_5 = "";

	private String spare_6 = "";
	
	private String who_to_rem = "";

	private String email_1 = "";

	private String who_to_rem_2 = "";

	private String email2 = "";

	private String user_id = "0";

	private Calendar posting_date = new GregorianCalendar();

	//private String posting_date = "";

	private String province = "";

	//private Calendar warrantyStartDate = new GregorianCalendar();

	private String warrantyStartDate = "";

	private String noOfMonths = "";

	private String raise_entry="N";

    private String status ="APPROVED";

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

	private MagmaDBConnection dbConnection;

 	private String amountPTD;

    private String amountREM;

    private String partPAY="N";

    private String fullyPAID="N";

    private String deferPay ="N";

    private String sbu_code ="";

    private String reject_reason="";

    private String vendorName="";


    public String getReject_reason() {
		return reject_reason;
	}

	public void setReject_reason(String reject_reason) {
		this.reject_reason = reject_reason;
	}

    public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	
	public String getRaise_entry() {
		return raise_entry;
	}

	public void setRaise_entry(String raise_entry) {
		this.raise_entry = raise_entry;
	}

	private String mtid="";
		public String getSbu_code() {
		return sbu_code;
	}

	public void setSbu_code(String sbu_code) {
		this.sbu_code = sbu_code;
	}

		AssetRecordsBean ad = new AssetRecordsBean();

       
	public GroupAssetBean() throws Exception
	{
		super();
		sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
		dbConnection = new MagmaDBConnection();
		dateFormat = new DatetimeFormat();
		htmlUtil = new HtmlUtility();
		comp = new CompanyHandler();
	}

	/**
	 * createGroup
	 *
	 * @return boolean
	 */


	public long createGroup(String groupAssetByAsset,String branch) throws Exception,Throwable
	{
    	
    	
		aprecords = new ApprovalRecords();
		System.out.println(">>>>>>>>> INSIDE CREATE GROUP IN GROUP ASSET BEAN <<<<<<<<<< ");
		
		StringBuffer b = new StringBuffer(400);
                StringBuffer sb = new StringBuffer(400);
		Codes code = new Codes();
		if (no_of_items == null || no_of_items.equals("")) {
			no_of_items = "0";
		}
		if (province == null || province.equals("")) {
			province = "0";
		}
		int itemsCount = Integer.parseInt(no_of_items);
		if (noOfMonths == null || noOfMonths.equals("")) {
			noOfMonths = "0";
		}
		if (branch_id == null || branch_id.equals("") || branch_id.equals("0")) {
			branch_id = branch;
		}

		if (warrantyStartDate == null || warrantyStartDate.equals("")) {
			warrantyStartDate = null;
		}
		if (expiryDate == null || expiryDate.equals("")) {
			expiryDate = null;
		}
		if(supervisor.equals("") ||supervisor=="0")
		{
			supervisor= user_id;
		}
		if (make == null || make.equals(""))
		{
			make="0";
		} 
		if (transport_cost == null || transport_cost.equals("")) {
			transport_cost = "0";
		}
		if (other_cost == null || other_cost.equals("")) {
			other_cost = "0";
		}
		System.out.println("vat_amount Before::: " + vat_amount);
		if (vat_amount != null || !vat_amount.equals("0")) {
			subject_to_vat = "Y";
		}
		if (vat_amount.equals("0")) {
			subject_to_vat = "N";
		}
		
		System.out.println("subject_to_vat after::: " + subject_to_vat);
//		System.out.println(">>>>>>>>>  transport_cost <<<<<<<<<< " +transport_cost+"    other_cost: "+other_cost+"    cost_price: "+cost_price);
	//	System.out.println(">>>>>>>>>  category_id <<<<<<<<<< " +category_id+"    sub_category_id: "+sub_category_id);
		String subcategoryId = sub_category_id;
		//System.out.println(">>>>>>>>> BEFORE CALLING CREATE GROUP MAIN IN GROUP ASSET BEAN <<<<<<<<<< ");
		long gid = createGroupMain();
//		ad.setPendingTrans(ad.setApprovalDataGroup(gid),"3"); 
		for (int x = 0; x < itemsCount; x++)
		{
			try {

			//asset_id = new legend.AutoIDSetup().getIdentity(branch_id,department_id, section_id, category_id);
	        if(groupAssetByAsset.equalsIgnoreCase("Y")){asset_id = new ApplicationHelper().getGeneratedId("am_group_asset");}
//			if(groupAssetByAsset.equalsIgnoreCase("N")){asset_id= new legend.AutoIDSetup().getIdentity(branch_id,department_id, section_id, category_id);}
//	          
       //                 con = dbConnection.getConnection("legendPlus");
	        System.out.println("<<<<< asset_id: " + asset_id);
	        
		String query = "INSERT INTO AM_GROUP_ASSET( ASSET_ID,"
			+ "REGISTRATION_NO,BRANCH_ID,DEPT_ID,"
			+ "CATEGORY_ID,SECTION_ID,DESCRIPTION,"
			+ "VENDOR_AC,DATE_PURCHASED,DEP_RATE,"
			+ "ASSET_MAKE,ASSET_MODEL,ASSET_SERIAL_NO,"
			+ "ASSET_ENGINE_NO,SUPPLIER_NAME,"
			+ "ASSET_USER,ASSET_MAINTENANCE,"
			+ "COST_PRICE,DEP_END_DATE,RESIDUAL_VALUE,"
			+ "AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,"
			+ "POSTING_DATE,EFFECTIVE_DATE,"
			+ "PURCHASE_REASON,LOCATION,VATABLE_COST,"
			+ "VAT,REQ_DEPRECIATION,SUBJECT_TO_VAT,"
			+ "WHO_TO_REM,	EMAIL1,	WHO_TO_REM_2,"
			+ "EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,[USER_ID], PROVINCE,"
			+ " WAR_START_DATE, WAR_MONTH, WAR_EXPIRY_DATE,BRANCH_CODE,DEPT_CODE,"
			+ "SECTION_CODE,CATEGORY_CODE,GROUP_ID,AMOUNT_PTD,AMOUNT_REM,ACCUM_DEP," +
                        "PART_PAY,FULLY_PAID,Asset_Status,"
			+ "supervisor,LPO,BAR_CODE,req_redistribution,Raise_entry,defer_pay,process_flag ," +
                        "SBU_CODE,post_flag,Invoice_no,workstationIp, SPARE_3, SPARE_4, SPARE_5, SPARE_6,sub_category_id,SUB_CATEGORY_CODE,"
			+ "INTEGRIFY,PROJECT_CODE,TRANPORT_COST,OTHER_COST,VENDOR_NAME)	VALUES " +
					"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
					"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		Connection con = dbConnection.getConnection("legendPlus");
        PreparedStatement ps = con.prepareStatement(query);
		//b.append(query);
		amountPTD = amountPTD.replaceAll(",","");
		//b.append(asset_id);
		ps.setString(1, asset_id);

//		b.append("','");
//		b.append(registration_no);
		ps.setString(2, registration_no);
		//b.append("',");
		//b.append(branch_id);
		ps.setString(3, branch_id);
		//b.append(",");
		//b.append(department_id);
		ps.setString(4, department_id);
		//b.append(",");
		//b.append(category_id);
		ps.setString(5, category_id);
		//b.append(",");
		//b.append(section_id);
		ps.setString(6, section_id);
		//b.append(",'");
		//b.append(description);
		ps.setString(7, description.toUpperCase());
		//b.append("','");
		//b.append(vendor_account);
		ps.setString(8, vendor_account);
		//b.append("','");
		//System.out.println("Date of Purchase : " +DateManipulations.CalendarToDb(date_of_purchase));
		//b.append(DateManipulations.CalendarToDb(date_of_purchase));
		ps.setString(9, DateManipulations.CalendarToDb(date_of_purchase));
		//b.append("',");
		//b.append(depreciation_rate); '"+technician+"'
        String TechQuery = "select dep_rate from am_ad_category where category_id = category_id";
        String rate = aprecords.getCodeName(TechQuery); 
		ps.setString(10, rate);
//		ps.setString(10, depreciation_rate);
//		b.append(",");
//		b.append(make);
		ps.setString(11, make);
//		b.append(",'");
//		b.append(model);
		ps.setString(12, model);
//		b.append("','");
//		b.append(serial_number);
		ps.setString(13, serial_number);
//		b.append("','");
//		b.append(engine_number);
		ps.setString(14, engine_number);
//		b.append("',");
//		b.append(supplied_by);
		ps.setInt(15, Integer.parseInt(supplied_by));
//		b.append(",'");
//		b.append(user);
		ps.setString(16, user);
//		b.append("',");
//		b.append(maintained_by);
		ps.setInt(17, Integer.parseInt(maintained_by));
//		b.append(",");  
//		b.append(Double.parseDouble(cost_price) / itemsCount);
//		double costPrice = Double.parseDouble(cost_price)+Double.parseDouble(transport_cost)+Double.parseDouble(other_cost);
		double costPrice = Double.parseDouble(cost_price);
//		System.out.println("costPrice ::: " + costPrice+"  cost_price ::: " + cost_price+"  itemsCount ::: " + itemsCount+"  transport_cost ::: " + transport_cost+"  other_cost ::: " + other_cost);
		ps.setDouble(18, costPrice / itemsCount);  
		//b.append(",'");
		// System.out.println(DateManipulations.CalendarToDb(date_of_purchase));
		//b.append(DateManipulations.CalendarToDb(depreciation_end_date));
		//b.append(dbConnection.dateConvert(depreciation_end_date));
		ps.setDate(19, dbConnection.dateConvert(depreciation_end_date));
//		b.append("',");
//		b.append(residual_value);
		ps.setDouble(20, Double.parseDouble(residual_value));
//		b.append(",'");
//		b.append(authorized_by);
		ps.setString(21, authorized_by);
//		b.append("','");
//		b.append(wh_tax_cb);
		ps.setString(22, wh_tax_cb);
//		b.append("',");
//		b.append(Double.parseDouble(wh_tax_amount) / itemsCount);
		ps.setDouble(23, Double.parseDouble(wh_tax_amount) / itemsCount);
//		b.append(",'");
//		b.append(DateManipulations.CalendarToDb(posting_date));
		ps.setString(24,DateManipulations.CalendarToDb(posting_date));
//		b.append("','");
//		b.append(dbConnection.dateConvert(depreciation_start_date));
		ps.setDate(25, dbConnection.dateConvert(depreciation_start_date));
//		b.append("','");
//		b.append(reason);
		ps.setString(26, reason);
//		b.append("',");
//		b.append(location);
		ps.setInt(27, Integer.parseInt(location));
//		b.append(",");
//		b.append(Double.parseDouble(vatable_cost) / itemsCount);
		ps.setDouble(28, Double.parseDouble(vatable_cost) / itemsCount);
//		b.append(",");
//		b.append(Double.parseDouble(vat_amount) / itemsCount);
		ps.setDouble(29, Double.parseDouble(vat_amount) / itemsCount);
//		b.append(",'");
//		b.append(require_depreciation);
		ps.setString(30, require_depreciation);
//		b.append("','");
//		b.append(subject_to_vat);
		ps.setString(31, subject_to_vat);
//		b.append("','");
//		b.append(who_to_rem);
		ps.setString(32, who_to_rem);
//		b.append("','");
//		b.append(email_1);
		ps.setString(33, email_1);
//		b.append("','");
//		b.append(who_to_rem_2);
		ps.setString(34, who_to_rem_2);
//		b.append("','");
//		b.append(email2);
		ps.setString(35, email2);
//		b.append("',");
//		b.append(state);
		ps.setInt(36, Integer.parseInt(state));
//		b.append(",");
//		b.append(driver);
		ps.setInt(37, Integer.parseInt(driver));
//		b.append(",'");
//		b.append(spare_1);
		ps.setString(38, spare_1);
//		b.append("','");
//		b.append(spare_2);
		ps.setString(39, spare_2);
//		b.append("',");
//		b.append(user_id + ",");
		ps.setString(40, user_id);
		//b.append(province);
		ps.setInt(41, Integer.parseInt(province));
//		b.append(",'");
//		b.append(dbConnection.dateConvert(warrantyStartDate));
		ps.setDate(42, dbConnection.dateConvert(warrantyStartDate));
//		b.append("',");
//		b.append(noOfMonths);
		ps.setInt(43, Integer.parseInt(noOfMonths));
//		b.append(",'");
//		b.append(dbConnection.dateConvert(expiryDate));
		ps.setDate(44, dbConnection.dateConvert(expiryDate));
//		b.append("','");
//		b.append(code.getBranchCode(branch_id));
		ps.setString(45,code.getBranchCode(branch_id));
		/*System.out.println("branch_id ::: " + branch_id);
		System.out.println("branch_id ::: " + code.getBranchCode(branch_id));*/
//		b.append("','");
//		b.append(code.getDeptCode(department_id));
		ps.setString(46,code.getDeptCode(department_id));
//		b.append("','");
//		b.append(code.getDeptCode(section_id));
		ps.setString(47,code.getSectionCode(section_id));
//		b.append("','");
//		b.append(code.getDeptCode(category_id));
		ps.setString(48,code.getCategoryCode(category_id));
		/*System.out.println("category_id ::: " + category_id);
		System.out.println("category_code ::: " + code.getCategoryCode(category_id));*/
//		b.append("',");
//		b.append(gid);
		ps.setLong(49, gid);
//		b.append(",");
//		b.append(Double.parseDouble(amountPTD)/ itemsCount);
		ps.setDouble(50, Double.parseDouble(amountPTD) / itemsCount);
//		b.append(",");
//		b.append((Double.parseDouble(cost_price) / itemsCount)-(Double.parseDouble(amountPTD)/ itemsCount));
		ps.setDouble(51, ((costPrice / itemsCount)-(Double.parseDouble(amountPTD)/ itemsCount)));
//		ps.setDouble(51, ((Double.parseDouble(cost_price) / itemsCount)-(Double.parseDouble(amountPTD)/ itemsCount)));
//		b.append(",");
//		b.append(accum_dep);
		ps.setDouble(52, accum_dep);
//		b.append(",'");
//		b.append(partPAY);
//		ps.setString(53,"N");
		ps.setString(53,partPAY);
//		b.append("','");
//		b.append(fullyPAID);
//		ps.setString(54,"Y");
		ps.setString(54,fullyPAID);
//		b.append("','");
//        b.append(status);
		status = "PENDING";
        ps.setString(55,status);
//        b.append("','");
//        b.append(supervisor);
        ps.setString(56,supervisor);
//        b.append("','");
//        b.append(lpo);
        ps.setString(57,lpo);
//        b.append("','");
//        b.append(bar_code);
        ps.setString(58,bar_code);
//        b.append("','");
//        b.append(require_redistribution);
        ps.setString(59,require_redistribution);
//        b.append("','");
//        b.append(raise_entry);
        ps.setString(60,"N");
//        b.append("')");
        ps.setString(61,deferPay);
        ps.setString(62,"N");
        ps.setString(63, sbu_code);
        ps.setString(64, "P");
        ps.setString(65, invoiceNum);
        ps.setString(66, workstationIp); 
		ps.setString(67, spare_3);
		ps.setString(68, spare_4);
		ps.setString(69, spare_5);
		ps.setString(70, spare_6);
//		System.out.println(">>>>>>subcategoryId: "+subcategoryId+"    sub_category_id: "+sub_category_id);
		ps.setString(71, subcategoryId);
		ps.setString(72,code.getSubCategoryCode(subcategoryId));
		ps.setString(73, integrifyId);
		ps.setString(74, projectCode);
		ps.setDouble(75, Double.parseDouble(transport_cost) / itemsCount);
		ps.setDouble(76, Double.parseDouble(other_cost) / itemsCount);
		ps.setString(77, vendorName);
		
        boolean result = ps.execute();
	archiveUpdate(asset_id,itemsCount,gid);
//	dbConnection.closeConnection(con, ps, rs);
			}

                        catch (Exception r) {
				System.out.println("INFO: Error creating group aset >>" + r);
			} 
        }
		//ad.updateGroupAssetStatus(Long.toString(gid));
		return gid;
	}

	public long createGroupUnclassified(String branch) throws Exception,Throwable
	{
    	
		aprecords = new ApprovalRecords();
//		System.out.println(">>>>>>>>> INSIDE CREATE GROUP IN GROUP ASSET BEAN <<<<<<<<<< ");
		StringBuffer b = new StringBuffer(400);
                StringBuffer sb = new StringBuffer(400);
		Codes code = new Codes();
		if (no_of_items == null || no_of_items.equals("")) {
			no_of_items = "0";
		}
		if (province == null || province.equals("")) {
			province = "0";
		}
		int itemsCount = Integer.parseInt(no_of_items);
		if (noOfMonths == null || noOfMonths.equals("")) {
			noOfMonths = "0";
		}
		if (warrantyStartDate == null || warrantyStartDate.equals("")) {
			warrantyStartDate = null;
		}
		if (expiryDate == null || expiryDate.equals("")) {
			expiryDate = null;
		}
		if(supervisor.equals("") ||supervisor=="0")
		{
			supervisor= user_id;
		}
		if (branch_id == null || branch_id.equals("") || branch_id.equals("0")) {
			branch_id = branch;
		}
		if (make == null || make.equals(""))
		{
			make="0";
		}
            status ="PENDING";
            System.out.println("vat_amount Before::: " + vat_amount);            
    		if (vat_amount != null || !vat_amount.equals("0")) {
    			subject_to_vat = "Y";
    		}
//    		System.out.println("subject_to_vat After::: " + subject_to_vat);            
		//System.out.println(">>>>>>>>> BEFORE CALLING CREATE GROUP MAIN IN GROUP ASSET BEAN <<<<<<<<<< ");
		long gid = createGroupMain();
		//System.out.println(">>>>>>>>> AFTER CALLING CREATE GROUP MAIN IN GROUP ASSET BEAN <<<<<<<<<< ");
		/*System.out.println(">>>>>>>>> BRANCH_ID <<<<<<<<<< " + branch_id);
		System.out.println(">>>>>>>>> DEPARTMENT_ID <<<<<<<<<< " + department_id);
		System.out.println(">>>>>>>>> SECTION_ID <<<<<<<<<< " + section_id);
		System.out.println(">>>>>>>>> CATEGORY_ID <<<<<<<<<< " + category_id);*/
		//System.out.println(">>>>>>>>> Posting Date CalendarToDate <<<<<<<<<< " + DateManipulations.CalendarToDate(posting_date));
		//System.out.println(">>>>>>>>> Posting Date CalendarToDb <<<<<<<<<< " + DateManipulations.CalendarToDb(posting_date));

		for (int x = 0; x < itemsCount; x++)
		{
			try {

			//asset_id = new legend.AutoIDSetup().getIdentity(branch_id,department_id, section_id, category_id);
			asset_id = new ApplicationHelper().getGeneratedId("am_group_asset");
                


//		System.out.println(">>>>>>>>>  GENERATED ASSET_ID <<<<<<<<<< " + asset_id);

		String query = "INSERT INTO AM_GROUP_ASSET_UNCAPITALIZED( ASSET_ID,"
			+ "REGISTRATION_NO,BRANCH_ID,DEPT_ID,"
			+ "CATEGORY_ID,SECTION_ID,DESCRIPTION,"
			+ "VENDOR_AC,DATE_PURCHASED,DEP_RATE,"
			+ "ASSET_MAKE,ASSET_MODEL,ASSET_SERIAL_NO,"
			+ "ASSET_ENGINE_NO,SUPPLIER_NAME,"
			+ "ASSET_USER,ASSET_MAINTENANCE,"
			+ "COST_PRICE,DEP_END_DATE,RESIDUAL_VALUE,"
			+ "AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,"
			+ "POSTING_DATE,EFFECTIVE_DATE,"
			+ "PURCHASE_REASON,LOCATION,VATABLE_COST,"
			+ "VAT,REQ_DEPRECIATION,SUBJECT_TO_VAT,"
			+ "WHO_TO_REM,	EMAIL1,	WHO_TO_REM_2,"
			+ "EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,[USER_ID], PROVINCE,"
			+ " WAR_START_DATE, WAR_MONTH, WAR_EXPIRY_DATE,BRANCH_CODE,DEPT_CODE,"
			+ "SECTION_CODE,CATEGORY_CODE,GROUP_ID,AMOUNT_PTD,AMOUNT_REM,ACCUM_DEP," +
                        "PART_PAY,FULLY_PAID,Asset_Status,"
			+ "supervisor,LPO,BAR_CODE,req_redistribution,Raise_entry,defer_pay,process_flag ," +
                        "SBU_CODE,post_flag,Invoice_no,workstationIp, SPARE_3, SPARE_4, SPARE_5, SPARE_6,sub_category_id,SUB_CATEGORY_CODE,"
			+ "INTEGRIFY,VENDOR_NAME )	VALUES " +
					"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
					"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection con = dbConnection.getConnection("legendPlus");
        PreparedStatement ps = con.prepareStatement(query);
		//b.append(query);
		amountPTD = amountPTD.replaceAll(",","");
		//b.append(asset_id);
		ps.setString(1, asset_id);
//		b.append("','");
//		b.append(registration_no);
		ps.setString(2, registration_no);
		//b.append("',");
		//b.append(branch_id);
		ps.setString(3, branch_id);
		//b.append(",");
		//b.append(department_id);
		ps.setString(4, department_id);
		//b.append(",");
		//b.append(category_id);
		ps.setString(5, category_id);
		//b.append(",");
		//b.append(section_id);
		ps.setString(6, section_id);
		//b.append(",'");
		//b.append(description);
		ps.setString(7, description.toUpperCase());
		//b.append("','");
		//b.append(vendor_account);
		ps.setString(8, vendor_account);
		//b.append("','");
		//System.out.println("Date of Purchase : " +DateManipulations.CalendarToDb(date_of_purchase));
		//b.append(DateManipulations.CalendarToDb(date_of_purchase));
		ps.setString(9, DateManipulations.CalendarToDb(date_of_purchase));
		//b.append("',");
		//b.append(depreciation_rate);
		ps.setString(10, depreciation_rate);
//		b.append(",");
//		b.append(make);
		ps.setString(11, make);
//		b.append(",'");
//		b.append(model);
		ps.setString(12, model);
//		b.append("','");
//		b.append(serial_number);
		ps.setString(13, serial_number);
//		b.append("','");
//		b.append(engine_number);
		ps.setString(14, engine_number);
//		b.append("',");
//		b.append(supplied_by);
		ps.setInt(15, Integer.parseInt(supplied_by));
//		b.append(",'");
//		b.append(user);
		ps.setString(16, user);
//		b.append("',");
//		b.append(maintained_by);
		ps.setInt(17, Integer.parseInt(maintained_by));
//		b.append(",");
//		b.append(Double.parseDouble(cost_price) / itemsCount);
		double costPrice = Double.parseDouble(cost_price)+Double.parseDouble(transport_cost)+Double.parseDouble(other_cost);
		ps.setDouble(18, costPrice / itemsCount);
		//b.append(",'");
		// System.out.println(DateManipulations.CalendarToDb(date_of_purchase));
		//b.append(DateManipulations.CalendarToDb(depreciation_end_date));
		//b.append(dbConnection.dateConvert(depreciation_end_date));
		ps.setDate(19, dbConnection.dateConvert(depreciation_end_date));
//		b.append("',");
//		b.append(residual_value);
		ps.setDouble(20, Double.parseDouble(residual_value));
//		b.append(",'");
//		b.append(authorized_by);
		ps.setString(21, authorized_by);
//		b.append("','");
//		b.append(wh_tax_cb);
		ps.setString(22, wh_tax_cb);
//		b.append("',");
//		b.append(Double.parseDouble(wh_tax_amount) / itemsCount);
		ps.setDouble(23, Double.parseDouble(wh_tax_amount) / itemsCount);
//		b.append(",'");
//		b.append(DateManipulations.CalendarToDb(posting_date));
		ps.setString(24,DateManipulations.CalendarToDb(posting_date));
//		b.append("','");
//		b.append(dbConnection.dateConvert(depreciation_start_date));
		ps.setDate(25, dbConnection.dateConvert(depreciation_start_date));
//		b.append("','");
//		b.append(reason);
		ps.setString(26, reason);
//		b.append("',");
//		b.append(location);
		ps.setInt(27, Integer.parseInt(location));
//		b.append(",");
//		b.append(Double.parseDouble(vatable_cost) / itemsCount);
		ps.setDouble(28, Double.parseDouble(vatable_cost) / itemsCount);
//		b.append(",");
//		b.append(Double.parseDouble(vat_amount) / itemsCount);
		ps.setDouble(29, Double.parseDouble(vat_amount) / itemsCount);
//		b.append(",'");
//		b.append(require_depreciation);
		ps.setString(30, require_depreciation);
//		b.append("','");
//		b.append(subject_to_vat);
		ps.setString(31, subject_to_vat);
//		b.append("','");
//		b.append(who_to_rem);
		ps.setString(32, who_to_rem);
//		b.append("','");
//		b.append(email_1);
		ps.setString(33, email_1);
//		b.append("','");
//		b.append(who_to_rem_2);
		ps.setString(34, who_to_rem_2);
//		b.append("','");
//		b.append(email2);
		ps.setString(35, email2);
//		b.append("',");
//		b.append(state);
		ps.setInt(36, Integer.parseInt(state));
//		b.append(",");
//		b.append(driver);
		ps.setInt(37, Integer.parseInt(driver));
//		b.append(",'");
//		b.append(spare_1);
		ps.setString(38, spare_1);
//		b.append("','");
//		b.append(spare_2);
		ps.setString(39, spare_2);
//		b.append("',");
//		b.append(user_id + ",");
		ps.setString(40, user_id);
		//b.append(province);
		ps.setInt(41, Integer.parseInt(province));
//		b.append(",'");
//		b.append(dbConnection.dateConvert(warrantyStartDate));
		ps.setDate(42, dbConnection.dateConvert(warrantyStartDate));
//		b.append("',");
//		b.append(noOfMonths);
		ps.setInt(43, Integer.parseInt(noOfMonths));
//		b.append(",'");
//		b.append(dbConnection.dateConvert(expiryDate));
		ps.setDate(44, dbConnection.dateConvert(expiryDate));
//		b.append("','");
//		b.append(code.getBranchCode(branch_id));
		ps.setString(45,code.getBranchCode(branch_id));
		/*System.out.println("branch_id ::: " + branch_id);
		System.out.println("branch_id ::: " + code.getBranchCode(branch_id));*/
//		b.append("','");
//		b.append(code.getDeptCode(department_id));
		ps.setString(46,code.getDeptCode(department_id));
//		b.append("','");
//		b.append(code.getDeptCode(section_id));
		ps.setString(47,code.getSectionCode(section_id));
//		b.append("','");
//		b.append(code.getDeptCode(category_id));
		ps.setString(48,code.getCategoryCode(category_id));
		/*System.out.println("category_id ::: " + category_id);
		System.out.println("category_code ::: " + code.getCategoryCode(category_id));*/
//		b.append("',");
//		b.append(gid);
		ps.setLong(49, gid);
//		b.append(",");
//		b.append(Double.parseDouble(amountPTD)/ itemsCount);
		ps.setDouble(50, Double.parseDouble(amountPTD) / itemsCount);
//		b.append(",");
//		b.append((Double.parseDouble(cost_price) / itemsCount)-(Double.parseDouble(amountPTD)/ itemsCount));
		ps.setDouble(51, ((costPrice / itemsCount)-(Double.parseDouble(amountPTD)/ itemsCount)));
//		b.append(",");
//		b.append(accum_dep);
		ps.setDouble(52, accum_dep);
//		b.append(",'");
//		b.append(partPAY);
		ps.setString(53,"N");
//		b.append("','");
//		b.append(fullyPAID);
		ps.setString(54,"Y");
//		b.append("','");
//        b.append(status);
        ps.setString(55,status);
//        b.append("','");
//        b.append(supervisor);
        ps.setString(56,supervisor);
//        b.append("','");
//        b.append(lpo);
        ps.setString(57,lpo);
//        b.append("','");
//        b.append(bar_code);
        ps.setString(58,bar_code);
//        b.append("','");
//        b.append(require_redistribution);
        ps.setString(59,require_redistribution);
//        b.append("','");
//        b.append(raise_entry);
        ps.setString(60,"N");
//        b.append("')");
        ps.setString(61,"N");
        ps.setString(62,"N");
        ps.setString(63, sbu_code);
        ps.setString(64, "P");
        ps.setString(65, invoiceNum);
        ps.setString(66, workstationIp);
		ps.setString(67, spare_3);
		ps.setString(68, spare_4);
		ps.setString(69, spare_5);
		ps.setString(70, spare_6);
		ps.setString(71, sub_category_id);
		ps.setString(72,code.getSubCategoryCode(sub_category_id));       
		ps.setString(73, integrifyId);
		ps.setString(74, vendorName);
        boolean result = ps.execute();

	archiveUpdate(asset_id,itemsCount,gid);
//	dbConnection.closeConnection(con, ps, rs);
			}

                        catch (Exception r) {
				System.out.println("INFO: Error creating group aset >>" + r);
			} 
        }
		//ad.updateGroupAssetStatus(Long.toString(gid));
		return gid;
	}


        public void archiveUpdate(String asset_id,int itemsCount,long gid)
        {
        	dbConnection = new MagmaDBConnection();
        	
            Codes code = new Codes();
            String query = "INSERT INTO AM_GROUP_ASSET_ARCHIVE( ASSET_ID,"
			+ "REGISTRATION_NO,BRANCH_ID,DEPT_ID,"
			+ "CATEGORY_ID,SECTION_ID,DESCRIPTION,"
			+ "VENDOR_AC,DATE_PURCHASED,DEP_RATE,"
			+ "ASSET_MAKE,ASSET_MODEL,ASSET_SERIAL_NO,"
			+ "ASSET_ENGINE_NO,SUPPLIER_NAME,"
			+ "ASSET_USER,ASSET_MAINTENANCE,"
			+ "COST_PRICE,DEP_END_DATE,RESIDUAL_VALUE,"
			+ "AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,"
			+ "POSTING_DATE,EFFECTIVE_DATE,"
			+ "PURCHASE_REASON,LOCATION,VATABLE_COST,"
			+ "VAT,REQ_DEPRECIATION,SUBJECT_TO_VAT,"
			+ "WHO_TO_REM,	EMAIL1,	WHO_TO_REM_2,"
			+ "EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,[USER_ID], PROVINCE,"
			+ " WAR_START_DATE, WAR_MONTH, WAR_EXPIRY_DATE,BRANCH_CODE,DEPT_CODE,"
			+ "SECTION_CODE,CATEGORY_CODE,GROUP_ID,AMOUNT_PTD,AMOUNT_REM,ACCUM_DEP," +
                        "PART_PAY,FULLY_PAID,Asset_Status,"
			+ "supervisor,LPO,BAR_CODE,req_redistribution,Raise_entry,defer_pay,process_flag ," +
                        "SBU_CODE,post_flag,Invoice_no,workstationIp, SPARE_3, SPARE_4, SPARE_5, SPARE_6,sub_category_id,SUB_CATEGORY_CODE,"
			+ "INTEGRIFY,TRANPORT_COST,OTHER_COST,VENDOR_NAME )	VALUES " +
					"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
					"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            try {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);
		//b.append(query);
		amountPTD = amountPTD.replaceAll(",","");
		//b.append(asset_id);
		ps.setString(1, asset_id);
//		b.append("','");
//		b.append(registration_no);
		ps.setString(2, registration_no);
		//b.append("',");
		//b.append(branch_id);
		ps.setString(3, branch_id);
		//b.append(",");
		//b.append(department_id);
		ps.setString(4, department_id);
		//b.append(",");
		//b.append(category_id);
		ps.setString(5, category_id);
		//b.append(",");
		//b.append(section_id);
		ps.setString(6, section_id);
		//b.append(",'");
		//b.append(description);
		ps.setString(7, description.toUpperCase());
		//b.append("','");
		//b.append(vendor_account);
		ps.setString(8, vendor_account);
		//b.append("','");
		//System.out.println("Date of Purchase : " +DateManipulations.CalendarToDb(date_of_purchase));
		//b.append(DateManipulations.CalendarToDb(date_of_purchase));
		ps.setString(9, DateManipulations.CalendarToDb(date_of_purchase));
		//b.append("',");
		//b.append(depreciation_rate);
		ps.setString(10, depreciation_rate);
//		b.append(",");
//		b.append(make);
		ps.setString(11, make);
//		b.append(",'");
//		b.append(model);
		ps.setString(12, model);
//		b.append("','");
//		b.append(serial_number);
		ps.setString(13, serial_number);
//		b.append("','");
//		b.append(engine_number);
		ps.setString(14, engine_number);
//		b.append("',");
//		b.append(supplied_by);
		ps.setInt(15, Integer.parseInt(supplied_by));
//		b.append(",'");
//		b.append(user);
		ps.setString(16, user);
//		b.append("',");
//		b.append(maintained_by);
		ps.setInt(17, Integer.parseInt(maintained_by));
//		b.append(",");
//		b.append(Double.parseDouble(cost_price) / itemsCount);
		double costPrice = Double.parseDouble(cost_price)+Double.parseDouble(transport_cost)+Double.parseDouble(other_cost);
		ps.setDouble(18, costPrice / itemsCount);
		//b.append(",'");
		// System.out.println(DateManipulations.CalendarToDb(date_of_purchase));
		//b.append(DateManipulations.CalendarToDb(depreciation_end_date));
		//b.append(dbConnection.dateConvert(depreciation_end_date));
		ps.setDate(19, dbConnection.dateConvert(depreciation_end_date));
//		b.append("',");
//		b.append(residual_value);
		ps.setDouble(20, Double.parseDouble(residual_value));
//		b.append(",'");
//		b.append(authorized_by);
		ps.setString(21, authorized_by);
//		b.append("','");
//		b.append(wh_tax_cb);
		ps.setString(22, wh_tax_cb);
//		b.append("',");
//		b.append(Double.parseDouble(wh_tax_amount) / itemsCount);
		ps.setDouble(23, Double.parseDouble(wh_tax_amount) / itemsCount);
//		b.append(",'");
//		b.append(DateManipulations.CalendarToDb(posting_date));
		ps.setString(24,DateManipulations.CalendarToDb(posting_date));
//		b.append("','");
//		b.append(dbConnection.dateConvert(depreciation_start_date));
		ps.setDate(25, dbConnection.dateConvert(depreciation_start_date));
//		b.append("','");
//		b.append(reason);
		ps.setString(26, reason);
//		b.append("',");
//		b.append(location);
		ps.setInt(27, Integer.parseInt(location));
//		b.append(",");
//		b.append(Double.parseDouble(vatable_cost) / itemsCount);
		ps.setDouble(28, Double.parseDouble(vatable_cost) / itemsCount);
//		b.append(",");
//		b.append(Double.parseDouble(vat_amount) / itemsCount);
		ps.setDouble(29, Double.parseDouble(vat_amount) / itemsCount);
//		b.append(",'");
//		b.append(require_depreciation);
		ps.setString(30, require_depreciation);
//		b.append("','");
//		b.append(subject_to_vat);
		ps.setString(31, subject_to_vat);
//		b.append("','");
//		b.append(who_to_rem);
		ps.setString(32, who_to_rem);
//		b.append("','");
//		b.append(email_1);
		ps.setString(33, email_1);
//		b.append("','");
//		b.append(who_to_rem_2);
		ps.setString(34, who_to_rem_2);
//		b.append("','");
//		b.append(email2);
		ps.setString(35, email2);
//		b.append("',");
//		b.append(state);
		ps.setInt(36, Integer.parseInt(state));
//		b.append(",");
//		b.append(driver);
		ps.setInt(37, Integer.parseInt(driver));
//		b.append(",'");
//		b.append(spare_1);
		ps.setString(38, spare_1);
//		b.append("','");
//		b.append(spare_2);
		ps.setString(39, spare_2);
//		b.append("',");
//		b.append(user_id + ",");
		ps.setString(40, user_id);
		//b.append(province);
		ps.setInt(41, Integer.parseInt(province));
//		b.append(",'");
//		b.append(dbConnection.dateConvert(warrantyStartDate));
		ps.setDate(42, dbConnection.dateConvert(warrantyStartDate));
//		b.append("',");
//		b.append(noOfMonths);
		ps.setInt(43, Integer.parseInt(noOfMonths));
//		b.append(",'");
//		b.append(dbConnection.dateConvert(expiryDate));
		ps.setDate(44, dbConnection.dateConvert(expiryDate));
//		b.append("','");
//		b.append(code.getBranchCode(branch_id));
		ps.setString(45,code.getBranchCode(branch_id));
		/*System.out.println("branch_id ::: " + branch_id);
		System.out.println("branch_id ::: " + code.getBranchCode(branch_id));*/
//		b.append("','");
//		b.append(code.getDeptCode(department_id));
		ps.setString(46,code.getDeptCode(department_id));
//		b.append("','");
//		b.append(code.getDeptCode(section_id));
		ps.setString(47,code.getSectionCode(section_id));
//		b.append("','");
//		b.append(code.getDeptCode(category_id));
		ps.setString(48,code.getCategoryCode(category_id));
		/*System.out.println("category_id ::: " + category_id);
		System.out.println("category_code ::: " + code.getCategoryCode(category_id));*/
//		b.append("',");
//		b.append(gid);
		ps.setLong(49, gid);
//		b.append(",");
//		b.append(Double.parseDouble(amountPTD)/ itemsCount);
		ps.setDouble(50, Double.parseDouble(amountPTD) / itemsCount);
//		b.append(",");
//		b.append((Double.parseDouble(cost_price) / itemsCount)-(Double.parseDouble(amountPTD)/ itemsCount));
		ps.setDouble(51, ((costPrice / itemsCount)-(Double.parseDouble(amountPTD)/ itemsCount)));
//		b.append(",");
//		b.append(accum_dep);
		ps.setDouble(52, accum_dep);
//		b.append(",'");
//		b.append(partPAY);
		ps.setString(53,partPAY);
//		b.append("','");
//		b.append(fullyPAID);
		ps.setString(54,fullyPAID);
//		b.append("','");
//        b.append(status);
        ps.setString(55,status);
//        b.append("','");
//        b.append(supervisor);
        ps.setString(56,supervisor);
//        b.append("','");
//        b.append(lpo);
        ps.setString(57,lpo);
//        b.append("','");
//        b.append(bar_code);
        ps.setString(58,bar_code);
//        b.append("','");
//        b.append(require_redistribution);
        ps.setString(59,require_redistribution);
//        b.append("','");
//        b.append(raise_entry);
        ps.setString(60,"N");
//        b.append("')");
        ps.setString(61,deferPay);
        ps.setString(62,"N");
        ps.setString(63, sbu_code);
        ps.setString(64, "P");
        ps.setString(65, invoiceNum);
        ps.setString(66, workstationIp);
		ps.setString(67, spare_3);
		ps.setString(68, spare_4);
		ps.setString(69, spare_5);
		ps.setString(70, spare_6);
		ps.setString(71, sub_category_id);
		ps.setString(72,code.getSubCategoryCode(sub_category_id));   
		ps.setString(73, integrifyId);
		ps.setDouble(74, Double.parseDouble(transport_cost) / itemsCount);
		ps.setDouble(75, Double.parseDouble(other_cost) / itemsCount);
		ps.setString(76, vendorName);
        boolean result = ps.execute();
//        dbConnection.closeConnection(con, ps, rs);
            }
catch (Exception r) {
				System.out.println("INFO: Error creating group aset in archive >>" + r);
			} 
        }




	public long createGroupMain() throws Exception {
    	
    	
		//System.out.println(">>>>>> INSIDE CREATE GROUP MAIN OF GROUP ASSET BEAN <<<<<<");
		StringBuffer b = new StringBuffer(400);
                StringBuffer sbf = new StringBuffer(400);
		Codes code = new Codes();
		if (no_of_items == null || no_of_items.equals("")) {
			no_of_items = "0";
		}
		if (province == null || province.equals("")) {
			province = "0";
		}
		int itemsCount = Integer.parseInt(no_of_items);
		if (noOfMonths == null || noOfMonths.equals("")) {
			noOfMonths = "0";
		}
		if (warrantyStartDate == null || warrantyStartDate.equals("")) {
			warrantyStartDate = null;
		}
		if (expiryDate == null || expiryDate.equals("")) {
			expiryDate = null;
		}
		if(supervisor.equals("")||supervisor=="0")
		{
			supervisor= user_id;
		}
		if (make == null || make.equals(""))
		{
			make="0";
		}
		long gid=0;
		double costPrice = Double.parseDouble(cost_price)+Double.parseDouble(transport_cost)+Double.parseDouble(other_cost);
//		System.out.println("Before Generating gid >>>>>>>>>> "+group_id);
//		String groupidQry = "select max(mt_id) from IA_MTID_TABLE where mt_tablename='am_asset_approval'";
		//System.out.println("After Generating gid >>>>>>>>>> "+groupidQry);
		//String groupid = aprecords.getCodeName(groupidQry);
		String groupid =  new ApplicationHelper().getGeneratedId("AM_GROUP_ASSET_MAIN"); 
		groupid = groupid+user_id;
//		System.out.println("After Generating gid >>>>>>>>>> "+groupid);
		 //double grpid = Double.parseDouble(group_id);
		 //gid = Long.parseLong(String.valueOf(grpid));
		 gid = Long.parseLong(groupid);
		 
		 int i =0;
		

	//the new query doesn't insert into Group_id since it is an identity column-ayojava

		String query = "SET IDENTITY_INSERT AM_GROUP_ASSET_MAIN ON  INSERT INTO AM_GROUP_ASSET_MAIN(GROUP_ID,QUANTITY,"
			+ "REGISTRATION_NO,BRANCH_ID,DEPT_ID,"
			+ "CATEGORY_ID,SECTION_ID,DESCRIPTION,"
			+ "VENDOR_AC,DATE_PURCHASED,DEP_RATE,ACCUM_DEP,"
			+ "ASSET_MAKE,ASSET_MODEL,ASSET_SERIAL_NO,"
			+ "ASSET_ENGINE_NO,SUPPLIER_NAME,"
			+ "ASSET_USER,ASSET_MAINTENANCE,"
			+ "COST_PRICE,DEP_END_DATE,RESIDUAL_VALUE,"
			+ "AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,"
			+ "POSTING_DATE,EFFECTIVE_DATE,"
			+ "PURCHASE_REASON,LOCATION,VATABLE_COST,"
			+ "VAT,REQ_DEPRECIATION,SUBJECT_TO_VAT,"
			+ "WHO_TO_REM,	EMAIL1,	WHO_TO_REM_2,"
			+ "EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,[USER_ID], PROVINCE,"
			+ " WAR_START_DATE, WAR_MONTH, WAR_EXPIRY_DATE,BRANCH_CODE,DEPT_CODE,"
			+ "SECTION_CODE,CATEGORY_CODE,raise_entry,AMOUNT_PTD,AMOUNT_REM,"
			+ "PART_PAY,FULLY_PAID,Asset_Status,supervisor,"
			+ "LPO,BAR_CODE,req_redistribution,defer_pay ,Vatable_Cost_Bal,process_flag," +
              "sbu_code,pend_GrpAssets,Invoice_No,workstationIp, SPARE_3, SPARE_4, SPARE_5, SPARE_6,sub_category_id,PROJECT_CODE,SUB_CATEGORY_CODE,"
			+ "TRANPORT_COST,OTHER_COST,VENDOR_NAME) VALUES(?,?,"
			+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, "
			+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, "
			+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, "
			+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, "
			+ "?,?,?,?,?,?,?,?,?,?,?,?) SET IDENTITY_INSERT AM_GROUP_ASSET_MAIN OFF";
//		System.out.println("=====b.toString(): "+b.toString());
	//	ad.setPendingTrans(ad.setApprovalDataGroup(Integer.parseInt(groupid)),"3"); 
		
		Connection con = dbConnection.getConnection("legendPlus");
        PreparedStatement ps = con.prepareStatement(query);
		  ps.setLong(1,gid);  
		  ps.setInt(2, itemsCount);
		  ps.setString(3, registration_no);
		  ps.setString(4, branch_id);
		  ps.setString(5, department_id);
		  ps.setString(6, category_id);
		  ps.setString(7, section_id);
		  ps.setString(8, description.toUpperCase());
		  ps.setString(9, vendor_account);
		  ps.setString(10, DateManipulations.CalendarToDb(date_of_purchase));
		  ps.setString(11, depreciation_rate);
		  ps.setDouble(12, accum_dep);
		  ps.setString(13, make);
		  ps.setString(14, model);
		  ps.setString(15, serial_number);
		  ps.setString(16, engine_number);
		  ps.setString(17, supplied_by);
		  ps.setString(18, user);
		  ps.setString(19, maintained_by);
		  ps.setDouble(20, costPrice);
		  ps.setDate(21, dbConnection.dateConvert(depreciation_end_date));
		  ps.setString(22, residual_value);
		  ps.setString(23, authorized_by);
		  ps.setString(24, wh_tax_cb);
		  ps.setDouble(25, Double.parseDouble(wh_tax_amount));
		  ps.setString(26, DateManipulations.CalendarToDb(posting_date));
		  ps.setDate(27, dbConnection.dateConvert(depreciation_start_date));
		  ps.setString(28, reason);
		  ps.setString(29, location);
		  ps.setDouble(30, Double.parseDouble(vatable_cost));
		  ps.setDouble(31, Double.parseDouble(vat_amount));
		  ps.setString(32, require_depreciation);
		  ps.setString(33, subject_to_vat);
		  ps.setString(34, who_to_rem);
		  ps.setString(35, email_1);
		  ps.setString(36, who_to_rem_2);
		  ps.setString(37, email2);
		  ps.setString(38, state);
		  ps.setString(39, driver);
		  ps.setString(40, spare_1);
		  ps.setString(41, spare_2);
		  ps.setString(42, user_id);
		  ps.setString(43, province);
		  ps.setDate(44, dbConnection.dateConvert(warrantyStartDate));
		  ps.setString(45, noOfMonths);
		  ps.setDate(46, dbConnection.dateConvert(expiryDate));
		  ps.setString(47, code.getBranchCode(branch_id));
		  ps.setString(48, code.getDeptCode(department_id));
		  ps.setString(49, code.getSectionCode(section_id));
		  ps.setString(50, code.getCategoryCode(category_id));
		  ps.setString(51, "N");  // Assuming "N" for raise_entry
		  ps.setDouble(52, Double.parseDouble(amountPTD));
		  ps.setDouble(53, costPrice - Double.parseDouble(amountPTD));
		  ps.setString(54, partPAY);
		  ps.setString(55, fullyPAID);
		  ps.setString(56, status);
		  ps.setString(57, supervisor);
		  ps.setString(58, lpo);
		  ps.setString(59, bar_code);
		  ps.setString(60, require_redistribution);
		  ps.setString(61, deferPay);
		  ps.setDouble(62, Double.parseDouble(vatable_cost)); // vatable_cost_2
		  ps.setString(63, "N");
		  ps.setString(64, sbu_code);
		  ps.setInt(65, itemsCount);
		  ps.setString(66, invoiceNum);
		  ps.setString(67, workstationIp);
		  ps.setString(68, spare_3);
		  ps.setString(69, spare_4);
		  ps.setString(70, spare_5);
		  ps.setString(71, spare_6);
		  ps.setString(72, sub_category_id);
		  ps.setString(73, projectCode);
		  ps.setString(74, code.getSubCategoryCode(sub_category_id));
		  ps.setDouble(75, Double.parseDouble(transport_cost));
		  ps.setDouble(76, Double.parseDouble(other_cost));
		  ps.setString(77, vendorName);

		  i = ps.executeUpdate();

			try
			{
				getStatement().executeUpdate(b.toString());
				//gid = retrieveMaxGroupID();
 //               String query_archive = "SET IDENTITY_INSERT AM_GROUP_ASSET_MAIN_Archive ON INSERT INTO AM_GROUP_ASSET_MAIN_Archive(group_id,QUANTITY,"

                        String query_archive = "INSERT INTO AM_GROUP_ASSET_MAIN_Archive(group_id,QUANTITY,"               		
			+ "REGISTRATION_NO,BRANCH_ID,DEPT_ID,"
			+ "CATEGORY_ID,SECTION_ID,DESCRIPTION,"
			+ "VENDOR_AC,DATE_PURCHASED,DEP_RATE,ACCUM_DEP,"
			+ "ASSET_MAKE,ASSET_MODEL,ASSET_SERIAL_NO,"
			+ "ASSET_ENGINE_NO,SUPPLIER_NAME,"
			+ "ASSET_USER,ASSET_MAINTENANCE,"
			+ "COST_PRICE,DEP_END_DATE,RESIDUAL_VALUE,"
			+ "AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,"
			+ "POSTING_DATE,EFFECTIVE_DATE,"
			+ "PURCHASE_REASON,LOCATION,VATABLE_COST,"
			+ "VAT,REQ_DEPRECIATION,SUBJECT_TO_VAT,"
			+ "WHO_TO_REM,	EMAIL1,	WHO_TO_REM_2,"
			+ "EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,[USER_ID], PROVINCE,"
			+ " WAR_START_DATE, WAR_MONTH, WAR_EXPIRY_DATE,BRANCH_CODE,DEPT_CODE,"
			+ "SECTION_CODE,CATEGORY_CODE,raise_entry,AMOUNT_PTD,AMOUNT_REM,"
			+ "PART_PAY,FULLY_PAID,Asset_Status,supervisor,"
			+ "LPO,BAR_CODE,req_redistribution,defer_pay ,Vatable_Cost_Bal,process_flag," +
                        "sbu_code,pend_GrpAssets,Invoice_No,workstationIp, SPARE_3, SPARE_4, SPARE_5, SPARE_6,PROJECT_CODE,sub_category_id,SUB_CATEGORY_CODE,"
			+ "TRANPORT_COST,OTHER_COST,VENDOR_NAME)	VALUES ( "
			+ "    ?, ?, ?, ?, ?, \r\n"
			+ "    ?, ?, ?, ?, ?, \r\n"
			+ "    ?, ?, ?, ?, ?, \r\n"
			+ "    ?, ?, ?, ?, ?, \r\n"
			+ "    ?, ?, ?, ?, ?, \r\n"
			+ "    ?, ?, ?, ?, ?, \r\n"
			+ "    ?, ?, ?, ?, ?, \r\n"
			+ "    ?, ?, ?, ?, ?, \r\n"
			+ "    ?, ?, ?, ?, ?, \r\n"
			+ "    ?, ?, ?, ?, ?, \r\n"
			+ "    ?, ?, ?, ?, ?, \r\n"
			+ "    ?, ?, ?, ?, ?, \r\n"
			+ "    ?, ?, ?, ?, ?, \r\n"
			+ "    ?, ?, ?, ?, ?, \r\n"
			+ "    ?, ?, ?, ?, ?, \r\n"
			+ "    ?, ?, ? "
			+ ")";
                        
                        
               // getStatement().executeUpdate(sbf.toString());
                        
                        Connection con2 = dbConnection.getConnection("legendPlus");
              		PreparedStatement  ps2 = con2.prepareStatement(query_archive);
              		ps2.setLong(1, gid); 
              		ps2.setInt(2, itemsCount);
              		ps2.setString(3, registration_no);
              		ps2.setString(4, branch_id);
              		ps2.setString(5, department_id);
              		ps2.setString(6, category_id);
              		ps2.setString(7, section_id);
              		ps2.setString(8, description.toUpperCase());
              		ps2.setString(9, vendor_account);
              		ps2.setString(10, DateManipulations.CalendarToDb(date_of_purchase));
              		ps2.setString(11, depreciation_rate);
              		ps2.setDouble(12, accum_dep);
              		ps2.setString(13, make);
              		ps2.setString(14, model);
              		ps2.setString(15, serial_number);
              		ps2.setString(16, engine_number);
              		ps2.setString(17, supplied_by);
              		ps2.setString(18, user);
              		ps2.setString(19, maintained_by);
              		ps2.setDouble(20, Double.parseDouble(cost_price));  // Assuming cost_price is a string
              		ps2.setDate(21, dbConnection.dateConvert(depreciation_end_date));
              		ps2.setString(22, residual_value);
              		ps2.setString(23, authorized_by);
              		ps2.setString(24, wh_tax_cb);
              		ps2.setDouble(25, Double.parseDouble(wh_tax_amount));
              		ps2.setString(26, DateManipulations.CalendarToDb(posting_date));
              		ps2.setDate(27, dbConnection.dateConvert(depreciation_start_date));
              		ps2.setString(28, reason);
              		ps2.setString(29, location);
              		ps2.setDouble(30, Double.parseDouble(vatable_cost));
              		ps2.setDouble(31, Double.parseDouble(vat_amount));
              		ps2.setString(32, require_depreciation);
              		ps2.setString(33, subject_to_vat);
              		ps2.setString(34, who_to_rem);
              		ps2.setString(35, email_1);
              		ps2.setString(36, who_to_rem_2);
              		ps2.setString(37, email2);
              		ps2.setString(38, state);
              		ps2.setString(39, driver);
              		ps2.setString(40, spare_1);
              		ps2.setString(41, spare_2);
              		ps2.setString(42, user_id);
              		ps2.setString(43, province);
              		ps2.setDate(44, dbConnection.dateConvert(warrantyStartDate));
              		ps2.setString(45, noOfMonths);
              		ps2.setDate(46, dbConnection.dateConvert(expiryDate));
              		ps2.setString(47, code.getBranchCode(branch_id));
              		ps2.setString(48, code.getDeptCode(department_id));
              		ps2.setString(49, code.getSectionCode(section_id));
              		ps2.setString(50, code.getCategoryCode(category_id));
              		ps2.setString(51, "N");  // Assuming "N" for raise_entry
              		ps2.setDouble(52, Double.parseDouble(amountPTD));
              		ps2.setDouble(53, (costPrice - Double.parseDouble(amountPTD)));
              		ps2.setString(54, partPAY);
              		ps2.setString(55, fullyPAID);
              		ps2.setString(56, status);
              		ps2.setString(57, supervisor);
              		ps2.setString(58, lpo);
              		ps2.setString(59, bar_code);
              		ps2.setString(60, require_redistribution);
              		ps2.setString(61, deferPay);
              		ps2.setDouble(62, Double.parseDouble(vatable_cost)); // vatable_cost_2
              		ps2.setString(63, "N");
              		ps2.setString(64, sbu_code);
              		ps2.setInt(65, itemsCount);
              		ps2.setString(66, invoiceNum);
              		ps2.setString(67, workstationIp);
              		ps2.setString(68, spare_3);
              		ps2.setString(69, spare_4);
              		ps2.setString(70, spare_5);
              		ps2.setString(71, spare_6);
              		ps2.setString(72, sub_category_id);
              		ps2.setString(73, projectCode);
              		ps2.setString(74, code.getSubCategoryCode(sub_category_id));
              		ps2.setDouble(75, Double.parseDouble(transport_cost));
              		ps2.setDouble(76, Double.parseDouble(other_cost));
              		ps2.setString(77, vendorName);
			}
			catch (Exception r)
			{
				System.out.println("INFO: Error creating AM_GROUP_ASSET_MAIN_Archive >>" + r.getMessage());
			}
			

		return gid;
	}


	private long retrieveMaxGroupID()
	{
		// TODO Auto-generated method stub
		Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        long maxNum=0;
		String groupID_qry = "select MAX(group_id) from am_group_asset_main";
		try
        {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(groupID_qry);
            rs = ps.executeQuery();
            if (rs.next())
            {
            	 maxNum = rs.getLong(1);
            }
//            System.out.println("Max GroupID retrieved : " + maxNum);
        }
		catch (Exception e)
        {
            System.out.println("INFO:Error retrieving Maximum GroupID ->" +
                               e.getMessage());
        }
		finally
        {
            dbConnection.closeConnection(con, ps, rs);
        }
		return maxNum;
	}


	public String getAsset_id() {
		return asset_id;
	}

	public String getAuthuser() {
		return authuser;
	}

	public void setAuthuser(String authuser) {
		this.authuser = authuser;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public void setAsset_id(String asset_id) {
		this.asset_id = asset_id;
	}




	public void setAccum_dep(String accum_dep) {
		if (accum_dep != null) {
            this.accum_dep = Double.parseDouble(accum_dep);
        }
	}

	public String getLpo() {
        if(lpo == null){
       lpo ="";
         }

       return lpo;
    }

    /**
     * @param lpo the lpo to set
     */
    public void setLpo(String lpo) {
         if (lpo != null) {
            this.lpo = lpo;

        }

    }

	public String getRequire_redistribution() {
		return require_redistribution;
	}

	public void setRequire_redistribution(String require_redistribution) {
		this.require_redistribution = require_redistribution;
	}

	public String getBar_code() {
        if(bar_code == null){
       bar_code ="";
         }

       return bar_code;
        }

    /**
     * @param bar_code the bar_code to set
     */
    public void setBar_code(String bar_code) {
    if (bar_code != null) {
            this.bar_code = bar_code.replaceAll("'", "");
        }

        //this.bar_code = bar_code;
    }


	 public double getAccum_dep() {
	        return accum_dep;
	    }

	public void setBranch_id(String branch_id) {
		if (branch_id != null) {
			this.branch_id = branch_id;
		}
	}

	public void setDepartment_id(String department_id) {
		if (department_id != null) {
			this.department_id = department_id;
		}
	}

	public void setSection_id(String section_id) {
		if (section_id != null) {
			this.section_id = section_id;
		}
	}

	public void setCategory_id(String category_id) {
		if (category_id != null) {
			this.category_id = category_id;
		}
	}

	public void setIntegrifyId(String integrifyId) {
		if (integrifyId != null) {
			this.integrifyId = integrifyId;
		}
	}
	
	public void setSub_category_id(String sub_category_id) {
		if (sub_category_id != null) {
			this.sub_category_id = sub_category_id;
		}
	}

	public void setMake(String make) {
		if (make != null) {
			this.make = make;
		}
	}

	public void setLocation(String location) {
		if (location != null) {
			this.location = location;
		}
	}

	public void setMaintained_by(String maintained_by) {
		if (maintained_by != null) {
			this.maintained_by = maintained_by;
		}
	}

	public void setDriver(String driver) {
		if (driver != null) {
			this.driver = driver;
		}
	}

	public void setState(String state) {
		if (state != null) {
			this.state = state;
		}
	}

	public void setSupplied_by(String supplied_by) {
		if (supplied_by != null) {
			this.supplied_by = supplied_by;
		}
	}

	public void setRegistration_no(String registration_no) {
		if (registration_no != null) {
			this.registration_no = registration_no;
		}
	}

	public void setDate_of_purchase(String date_of_purchase) {
		if (date_of_purchase != null) {
			this.date_of_purchase = DateManipulations
					.DateToCalendar(date_of_purchase);
		}
	}

	public void setDepreciation_start_date(String depreciation_start_date) {
		if (depreciation_start_date != null) {
			this.depreciation_start_date = depreciation_start_date;
		}
	}

	public void setDepreciation_end_date(String depreciation_end_date) {
		if (depreciation_end_date != null) {
			this.depreciation_end_date = depreciation_end_date;
		}
	}

	public void setAuthorized_by(String authorized_by) {
		if (authorized_by != null) {
			this.authorized_by = authorized_by;
		}
	}

	public void setReason(String reason) {
		if (reason != null) {
			this.reason = reason;
		}
	}

	public void setNo_of_items(String no_of_items) {
		if (no_of_items != null) {
			this.no_of_items = no_of_items.replaceAll(",", "");
		}
	}

	public void setDescription(String description) {
		if (description != null) {
			this.description = description;
		}
	}

	public void setCost_price(String cost_price) {
		if (cost_price != null) {
			this.cost_price = cost_price.replaceAll(",", "");
		}
	}

	public void setTransport_cost(String transport_cost) {
		if (transport_cost != null) {
			this.transport_cost = transport_cost.replaceAll(",", "");
		}
	}

	public void setOther_cost(String other_cost) {
		if (other_cost != null) {
			this.other_cost = other_cost.replaceAll(",", "");
		}
	}


	public void setVatable_cost(String vatable_cost) {
		if (vatable_cost != null) {
			this.vatable_cost = vatable_cost.replaceAll(",", "");
		}
	}

	public void setSubject_to_vat(String subject_to_vat) {
		if (subject_to_vat != null) {
			this.subject_to_vat = subject_to_vat.replaceAll(",", "");
		}
	}

	public void setVat_amount(String vat_amount) {
		if (vat_amount != null) {
			this.vat_amount = vat_amount.replaceAll(",", "");
		}
	}

	public void setWh_tax_cb(String wh_tax_cb) {
		if (wh_tax_cb != null) {
			this.wh_tax_cb = wh_tax_cb;
		}
	}

	public void setWh_tax_amount(String wh_tax_amount) {
		if (wh_tax_amount != null) {
			this.wh_tax_amount = wh_tax_amount.replaceAll(",", "");
		}
	}

	public void setSerial_number(String serial_number) {
		if (serial_number != null) {
			this.serial_number = serial_number;
		}
	}

	public void setEngine_number(String engine_number) {
		if (engine_number != null) {
			this.engine_number = engine_number;
		}
	}

	public void setModel(String model) {
		if (model != null) {
			this.model = model;
		}
	}

	public void setUser(String user) {
		if (user != null) {
			this.user = user;
		}
	}

	public void setDepreciation_rate(String depreciation_rate) {
		if (depreciation_rate != null) {
			this.depreciation_rate = depreciation_rate.replaceAll(",", "");
		}
	}

	public void setResidual_value(String residual_value) {
		if (residual_value != null) {
			this.residual_value = residual_value.replaceAll(",", "");
		}
	}

	public void setRequire_depreciation(String require_depreciation) {
		if (require_depreciation != null) {
			this.require_depreciation = require_depreciation;
		}
	}

	public void setVendor_account(String vendor_account) {
		if (vendor_account != null) {
			this.vendor_account = vendor_account;
		}
	}

	public void setSpare_1(String spare_1) {
		if (spare_1 != null) {
			this.spare_1 = spare_1;
		}
	}

	public void setSpare_2(String spare_2) {
		if (spare_2 != null) {
			this.spare_2 = spare_2;
		}
	}

	public void setSpare_3(String spare_3) {
		if (spare_3 != null) {
			this.spare_3 = spare_3;
		}
	}

	public void setSpare_4(String spare_4) {
		if (spare_4 != null) {
			this.spare_4 = spare_4;
		}
	}

	public void setSpare_5(String spare_5) {
		if (spare_5 != null) {
			this.spare_5 = spare_5;
		}
	}

	public void setSpare_6(String spare_6) {
		if (spare_6 != null) {
			this.spare_6 = spare_6;
		}
	}

	public void setWho_to_rem(String who_to_rem) {
		if (who_to_rem != null) {
			this.who_to_rem = who_to_rem;
		}
	}

	public void setEmail_1(String email_1) {
		if (email_1 != null) {
			this.email_1 = email_1;
		}
	}

	public void setWho_to_rem_2(String who_to_rem_2) {
		if (who_to_rem_2 != null) {
			this.who_to_rem_2 = who_to_rem_2;
		}
	}

	public void setEmail2(String email2) {
		if (email2 != null) {
			this.email2 = email2;
		}
	}

	public void setUser_id(String user_id) {
		if (user_id != null) {
			this.user_id = user_id;
		}
	}

	public void setPosting_date(String posting_date) {
		 //this.posting_date = posting_date;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setWarrantyStartDate(String warDate) {
        if (warrantyStartDate != null) {
            this.warrantyStartDate = warDate;
        }
    }

	public void setNoOfMonths(String months) {
		if (noOfMonths != null) {
			this.noOfMonths = months;
		}
	}

	public String getBranch_id() {
		return branch_id;
	}

	public String getDepartment_id() {
		return department_id;
	}

	public String getSection_id() {
		return section_id;
	}

	public String getCategory_id() {
		return category_id;
	}

	public String getIntegrifyId() {
		return integrifyId;
	}

	public String getSub_category_id() {
		return sub_category_id;
	}	

	public String getMake() {
		return make;
	}

	public String getLocation() {
		return location;
	}

	public String getMaintained_by() {
		return maintained_by;
	}

	public String getDriver() {
		return driver;
	}

	public String getState() {
		return state;
	}

	public String getSupplied_by() {
		return supplied_by;
	}

	public String getRegistration_no() {
		return registration_no;
	}

	public String getDate_of_purchase()
	{
		return DateManipulations.CalendarToDate2(date_of_purchase);
	}

	public String getDepreciation_start_date() {
		return depreciation_start_date;
	}

	public String getDepreciation_end_date() {
		return depreciation_end_date;
	}

	public String getAuthorized_by() {
		return authorized_by;
	}

	public String getReason() {
		return reason;
	}

	public String getNo_of_items() {
		return no_of_items;
	}

	public String getDescription() {
		return description;
	}

	public String getCost_price() {
		return cost_price;
	}

	public String getTransport_cost() {
		return transport_cost;
	}

	public String getOther_cost() {
		return other_cost;
	}

	public String getVatable_cost() {
		return vatable_cost;
	}

	public String getSubject_to_vat() {
		return subject_to_vat;
	}

	public String getVat_amount() {
		return vat_amount;
	}

	public String getWh_tax_cb() {
		return wh_tax_cb;
	}

	public String getWh_tax_amount() {
		return wh_tax_amount;
	}

	public String getSerial_number() {
		return serial_number;
	}

	public String getEngine_number() {
		return engine_number;
	}

	public String getModel() {
		return model;
	}

	public String getUser() {
		return user;
	}

	public String getDepreciation_rate() {
		return depreciation_rate;
	}

	public String getResidual_value() {
		return residual_value;
	}

	public String getRequire_depreciation() {
		return require_depreciation;
	}

	public String getVendor_account() {
		return vendor_account;
	}

	public String getSpare_1() {
		return spare_1;
	}

	public String getSpare_2() {
		return spare_2;
	}

	public String getSpare_3() {
		return spare_3;
	}

	public String getSpare_4() {
		return spare_4;
	}

	public String getSpare_5() {
		return spare_5;
	}

	public String getSpare_6() {
		return spare_6;
	}

	public String getWho_to_rem() {
		return who_to_rem;
	}

	public String getEmail_1() {
		return email_1;
	}

	public String getWho_to_rem_2() {
		return who_to_rem_2;
	}

	public String getEmail2() {
		return email2;
	}

	public String getUser_id() {
		return user_id;
	}

	public String getProvince() {
		return province;
	}

	public String getPosting_date() {
		return DateManipulations.CalendarToDate2(posting_date);
	}

	 public String getWarrantyStartDate() {
	        return warrantyStartDate;
	    }

	public String getNoOfMonths() {
		return noOfMonths;
	}

	public String getExpiryDate() {
        return expiryDate;
    }

	 public String getDeferPay() {
			return deferPay;
		}

		public void setDeferPay(String deferPay) {
			this.deferPay = deferPay;
		}

  

	/**
	 * getResidualvalue
	 *
	 * @return String
	 * @throws Exception
	 */
	public String getResidualvalue() throws Exception {
    	
		String selectQuery = "SELECT RESIDUAL_VALUE FROM AM_GB_COMPANY";
		String residualValue = "0.00";
		try {

			Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(selectQuery);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			 {
				residualValue = rs.getString(1);
			 }
		} catch (Exception e) {
			System.out.println("INFO: Error getting residualValue >>" + e);
		} 

		return residualValue;

	}

	public String getCreatedGroupId(String category, String description) {

		String id = "";
		String selectQuery = "SELECT ASSET_ID FROM AM_GROUP_ASSET "
				+ "WHERE CATEGORY_ID = " + category + " AND DESCRIPTION = '"
				+ description + "'";

		try {
			Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(selectQuery);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			 {
				id = rs.getString(1);
			 }

		} catch (Exception er) {
			System.out.println("INFO: Error fetching groupid >>" + er);
			id = "";
		} 

		return id;
	}

	public String getCreateStockdGroupId(String category, String description) {
    	
		String id = "";
		String selectQuery = "SELECT ASSET_ID FROM AM_GROUP_STOCK "
				+ "WHERE CATEGORY_ID = " + category + " AND DESCRIPTION = '"
				+ description + "'";

		try {
			Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(selectQuery);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			 {
				id = rs.getString(1);
			 }
		} catch (Exception er) {
			System.out.println("INFO: Error fetching Stock groupid >>" + er);
			id = "";
		} 

		return id;
	}

	public String getCreatedAsset2GroupId(String category, String description) {
    	
		String id = "";
		String selectQuery = "SELECT ASSET_ID FROM AM_GROUP_ASSET2 "
				+ "WHERE CATEGORY_ID = " + category + " AND DESCRIPTION = '"
				+ description + "'";

		try {
			Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(selectQuery);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			 {
				id = rs.getString(1);
			 }
		} catch (Exception er) {
			System.out.println("INFO: Error fetching groupid in Asset2 >>" + er);
			id = "";
		} 

		return id;
	}
	
	public void getCreatedGroup(long id) {
    	

		//String id = "";
		String selectQuery = "SELECT group_id, quantity, Registration_No, " +
				"Branch_ID,province, Dept_ID, Category_ID,Sub_Category_ID, section_id, Description, " +
				"Vendor_AC, Date_purchased, dep_rate, asset_make, asset_model, " +
				"asset_serial_no, asset_engine_no, supplier_name, asset_user, " +
				"asset_maintenance, Cost_Price, dep_end_date, residual_value, " +
				"authorized_by, wh_tax, wh_tax_amount, req_redistribution, " +
				"Posting_Date, effective_date, purchase_reason, location, " +
				"Vatable_Cost,TRANPORT_COST,OTHER_COST, Vat, Req_Depreciation, Subject_TO_Vat, " +
				"Who_TO_Rem, email1, who_to_rem_2, email2, Raise_entry, " +
				"dep_ytd, Section, Asset_Status, state, driver, spare_1, " +
				"spare_2, spare_3,spare_4,spare_5,spare_6, user_ID,WAR_START_DATE, WAR_MONTH, " +
				"WAR_EXPIRY_DATE, branch_code, dept_code, section_code, " +
				"category_code,AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID,ACCUM_DEP," +
                                "LPO,BAR_CODE,defer_pay,SBU_CODE,Invoice_no,PROJECT_CODE  " +
				" FROM am_group_asset_main WHERE GROUP_ID="+id;

		System.out.println("");
		//System.out.println("========================================");
		//System.out.println("Query in getCreatedGroup " + selectQuery);
		//System.out.println("========================================");
	//	System.out.println("getCreatedGroup selectQuery  "+selectQuery);
		try {
			Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(selectQuery);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
				{
		//		System.out.println("=========== VALUES OBTAINED FROM THE DATABASE ===============");
				group_id =rs.getString("GROUP_ID");
				//System.out.println("group_id >>>>>>> " + group_id);
				no_of_items=rs.getString("quantity");
				//System.out.println("no_of_items >>>>>>> " + no_of_items);
				registration_no = rs.getString("REGISTRATION_NO");
				//System.out.println("registration_no >>>>>>> " + registration_no);
                branch_id = rs.getString("BRANCH_ID");
                //System.out.println("branch_id >>>>>>> " + branch_id);
                province = rs.getString("PROVINCE");
                //System.out.println("province >>>>>>> " + province);
                department_id = rs.getString("DEPT_ID");
                //System.out.println("department_id >>>>>>> " + department_id);
                category_id = rs.getString("CATEGORY_ID");
                //System.out.println("category_id >>>>>>> " + category_id);
                section_id = rs.getString("SECTION_ID");
                //System.out.println("section_id >>>>>>> " + section_id);
                description = rs.getString("DESCRIPTION");
                //System.out.println("description >>>>>>> " + description);
                vendor_account = rs.getString("VENDOR_AC");
                //System.out.println("vendor_account >>>>>>> " + vendor_account);
                depreciation_rate = rs.getString("DEP_RATE");
                //System.out.println("depreciation_rate >>>>>>> " + depreciation_rate);
                make = rs.getString("ASSET_MAKE");
                //System.out.println("make >>>>>>> " + make);
                model = rs.getString("ASSET_MODEL");
                //System.out.println("model >>>>>>> " + model);
                serial_number = rs.getString("ASSET_SERIAL_NO");
                //System.out.println("serial_number >>>>>>> " + serial_number);
                engine_number = rs.getString("ASSET_ENGINE_NO");
                //System.out.println("engine_number >>>>>>> " + engine_number);
                supplied_by = rs.getString("SUPPLIER_NAME");
                //System.out.println("supplied_by >>>>>>> " + supplied_by);
                authuser = rs.getString("ASSET_USER");
                //System.out.println("authuser >>>>>>> " + authuser);
                maintained_by = rs.getString("ASSET_MAINTENANCE");
                //System.out.println("maintained_by >>>>>>> " + maintained_by);
                cost_price = rs.getString("COST_PRICE");
                transport_cost = rs.getString("TRANPORT_COST");
                other_cost = rs.getString("OTHER_COST");
                //System.out.println("cost_price >>>>>>> " + cost_price);
                depreciation_end_date = dbConnection.formatDate(rs.getDate("DEP_END_DATE"));
                //System.out.println("depreciation_end_date >>>>>>> " + depreciation_end_date);
                residual_value = rs.getString("RESIDUAL_VALUE");
                //System.out.println("residual_value >>>>>>> " + residual_value);
                authorized_by = rs.getString("AUTHORIZED_BY");
                //System.out.println("authorized_by >>>>>>> " + authorized_by);
                wh_tax_cb = rs.getString("WH_TAX");
                //System.out.println("wh_tax_cb >>>>>>> " + wh_tax_cb);
                wh_tax_amount = rs.getString("WH_TAX_AMOUNT");
                //System.out.println("wh_tax_amount >>>>>>> " + wh_tax_amount);
                require_redistribution = rs.getString("REQ_REDISTRIBUTION");
                //System.out.println("require_redistribution >>>>>>> " + require_redistribution);
                //posting_date = dbConnection.formatDate(rs.getDate("DATE_PURCHASED"));
                posting_date =DateManipulations.DateToCalendar(sdf.format(rs.getDate("POSTING_DATE")));
                //System.out.println("posting_date >>>>>>> " + posting_date);
                depreciation_start_date = dbConnection.formatDate(rs.getDate("EFFECTIVE_DATE"));
              //  System.out.println("depreciation_start_date >>>>>>> " + depreciation_start_date);
                reason = rs.getString("PURCHASE_REASON");
                //System.out.println("reason >>>>>>> " + reason);
                location = rs.getString("LOCATION");
                //System.out.println("location >>>>>>> " + location);
                vatable_cost = rs.getString("VATABLE_COST");
                //System.out.println("vatable_cost >>>>>>> " + vatable_cost);
                vat_amount = rs.getString("VAT");
                //System.out.println("vat_amount >>>>>>> " + vat_amount);
                require_depreciation = rs.getString("REQ_DEPRECIATION");
                //System.out.println("require_depreciation >>>>>>> " + require_depreciation);
                subject_to_vat = rs.getString("SUBJECT_TO_VAT");
               //System.out.println("subject_to_vat >>>>>>> " + subject_to_vat);
                who_to_rem = rs.getString("WHO_TO_REM");
                //System.out.println("who_to_rem >>>>>>> " + who_to_rem);
                email_1 = rs.getString("EMAIL1");
                //System.out.println("email_1 >>>>>>> " + email_1);
                who_to_rem_2 = rs.getString("WHO_TO_REM_2");
                //System.out.println("who_to_rem_2 >>>>>>> " + who_to_rem_2);
                email2 = rs.getString("EMAIL2");
                //System.out.println("email2 >>>>>>> " + email2);
                raise_entry = rs.getString("RAISE_ENTRY");
                //System.out.println("raise_entry >>>>>>> " + raise_entry);
                status = rs.getString("ASSET_STATUS");
                //System.out.println("status >>>>>>> " + status);
                state = rs.getString("STATE");
                //System.out.println("state >>>>>>> " + state);
                driver = rs.getString("DRIVER");
                //System.out.println("driver >>>>>>> " + driver);
                spare_1 = rs.getString("SPARE_1");
                //System.out.println("spare_1 >>>>>>> " + spare_1);
                spare_2 = rs.getString("SPARE_2");
                //System.out.println("spare_2 >>>>>>> " + spare_2);
                spare_3 = rs.getString("SPARE_3");
                spare_4 = rs.getString("SPARE_4");
                spare_5 = rs.getString("SPARE_5");
                spare_6 = rs.getString("SPARE_6");
                user = rs.getString("USER_ID");
                //System.out.println("user >>>>>>> " + user);
                warrantyStartDate = dbConnection.formatDate(rs.getDate(
                "WAR_START_DATE"));
               // System.out.println("warrantyStartDate >>>>>>> " + warrantyStartDate);
                noOfMonths = rs.getString("WAR_MONTH");
                //System.out.println("noOfMonths >>>>>>> " + noOfMonths);
                expiryDate =  dbConnection.formatDate(rs.getDate("WAR_EXPIRY_DATE"));
                //System.out.println("expiryDate >>>>>>> " + expiryDate);
                amountPTD = rs.getString("AMOUNT_PTD");
                //System.out.println("amountPTD >>>>>>> " + amountPTD);
                amountREM = rs.getString("AMOUNT_REM");
                //System.out.println("amountREM >>>>>>> " + amountREM);
                partPAY = rs.getString("PART_PAY");
                //System.out.println("partPAY >>>>>>> " + partPAY);
                fullyPAID = rs.getString("FULLY_PAID");
                //System.out.println("fullyPAID >>>>>>> " + fullyPAID);
                accum_dep = rs.getDouble("ACCUM_DEP");
                //System.out.println("accum_dep >>>>>>> " + accum_dep);
//              depreciation_start_date = DateManipulations.DateToCalendar(sdf.format(rs.getDate("EFFECTIVE_DATE")));
//              depreciation_end_date =DateManipulations.DateToCalendar(sdf.format(rs.getDate("DEP_END_DATE")));
                //asset_id = rs.getString("ASSET_ID");
                date_of_purchase =DateManipulations.DateToCalendar(sdf.format(rs.getDate("DATE_PURCHASED")));
                //System.out.println("date_of_purchase >>>>>>> " + date_of_purchase);
                //posting_date =dbConnection.formatDate(rs.getDate("POSTING_DATE"));
                //System.out.println("posting_date >>>>>>> " + posting_date);
                lpo=rs.getString("LPO");
                //System.out.println("lpo =====  " + lpo);
                bar_code=rs.getString("BAR_CODE");
                //System.out.println("BAR_CODE ==== " + bar_code);
                deferPay= rs.getString("defer_pay");
                //System.out.println("defer_Pay ==== " + deferPay);
                sbu_code= rs.getString("SBU_CODE");
                //System.out.println("=====================================");
		invoiceNum= rs.getString("Invoice_no");
        projectCode= rs.getString("PROJECT_CODE");
                        }
			//freeResource();
//			dbConnection.closeConnection(con, ps, rs);
		} catch (Exception er) {
			System.out.println("INFO: Error fetching groupid >>" + er);
			//id = "";
		} 

		//return id;
	}

	public long [] insertGroupAssetRecord(String groupAssetByAsset,String singleApproval,String branch,String departCode,String userName) throws Exception, Throwable {
		//System.out.println("INSIDE INSERT GROUP ASSET RECORD OF GROUP ASSET BEAN");
		System.out.println("<<<<<<<<<<<<singleApproval: "+singleApproval+"    <<<<branch: "+branch+"    <<<<departCode: "+departCode+"    <<<<userName: "+userName+"    <<<<groupAssetByAsset: "+groupAssetByAsset);
		 java.util.ArrayList approvelist =ad.getApprovalsId(branch,departCode,userName);
		 System.out.println("<<<<< approvelist.size() " + approvelist.size());;
		String[] budget = getBudgetInfo();
		double[] bugdetvalues = getBudgetValues();
		int DONE = 0; // everything is oK
		int BUDGETENFORCED = 1; // EF budget = Yes, CF = NO, ERROR_FLAG
		int BUDGETENFORCEDCF = 2; // EF budget = Yes, CF = Yes, ERROR_FLAG
		int ASSETPURCHASEBD = 3; // asset falls into no quarter purchase date

		long [] value = new long[2];

		// older than bugdet
		String Q = getQuarter();
		//System.out.println("BUDGET VALUE  =======  "  + budget[3]);
		if(budget[3].equalsIgnoreCase("N")){
			value[1]=createGroup(groupAssetByAsset,branch);
			value[0]= DONE;
		}
		else if(budget[3].equalsIgnoreCase("Y")){
		if (!Q.equalsIgnoreCase("NQ")) {
			if (budget[3].equalsIgnoreCase("Y")
					&& budget[4].equalsIgnoreCase("N")) {
				if (chkBudgetAllocation(Q, bugdetvalues, false)) {
					updateBudget(Q, budget);
					value[1]=createGroup(groupAssetByAsset,branch);
					value[0]= DONE;
				} else {
					value[0]=  BUDGETENFORCED;
				}

			} else if (budget[4].equalsIgnoreCase("Y")) {
				if (chkBudgetAllocation(Q, bugdetvalues, true)) {
					updateBudget(Q, budget);
					value[1]=createGroup(groupAssetByAsset,branch);
					value[0]=  DONE;
				} else {
					value[0]=  BUDGETENFORCEDCF;
				}

			} else {
				value[1]=createGroup(groupAssetByAsset,branch);
				value[0]= DONE;
			}
		} else {
			//value[1]=createGroup();
			value[0]= ASSETPURCHASEBD;
		}
	}
	else{}
		if(groupAssetByAsset.equalsIgnoreCase("N")){
		String groupId = String.valueOf(value[1]);
		int assetCode = 0;
        String page1 = "";
        String flag= "";
  	  	String partPay="";
	   	String subjectT= "";
	   	String whT= "";
  	   	String Name = "";
  	   	String url = "";
  	   	String supervisorName = "";
  	  	String mailAddress = "";
  //	  	String qryBranch =" SELECT branch_name from am_ad_branch where branch_id='"+brancId+"'";
  	   	String Branch = "";
//  	   	String subjectT= adGroup.subjectToVat(group_id);
//  	   	String whT= adGroup.whTax(group_id);

  	   	
//		System.out.println("<<<<<<<<<<<<catid: "+groupAssetByAsset+"    <<<<groupAssetByAsset: "+groupId);
	     java.util.ArrayList catlist =getAsstCategoryId(groupId);
	     java.util.ArrayList AssetIdGenlist =getAsstIdGeneration(groupId);
	     String assetRaiseEntry =htmlUtil.getCodeName(" SELECT raise_entry from am_gb_company ");
//	     System.out.println("<<<<<<<< Group By Category List Number>> "+catlist.size() );
//	     System.out.println("<<<<<<<< Asset By Id List Number>> "+AssetIdGenlist.size() );
//	     boolean duplicate = comp.raisentry_postDuplicate();
//	     boolean duplicate2 = comp.asset_approvalDuplicate();
	      //================================
	     for(int i=0;i<catlist.size();i++)
	     {
	     com.magbel.legend.vao.ViewAssetDetails  assetcat = (com.magbel.legend.vao.ViewAssetDetails)catlist.get(i);    	 
			int catid =  assetcat.getId();
			double depRate = assetcat.getDepRate();
//		System.out.println("<<<<<<<<<<<<catid: "+catid+"    <<<<depRate: "+depRate);	
			groupAssetRateGen(groupId,catid,depRate);
	     }    
	     for(int i=0;i<AssetIdGenlist.size();i++)
	     {
	    	 magma.net.vao.Asset  assetIdGen = (magma.net.vao.Asset)AssetIdGenlist.get(i);    
	     	int Id = Integer.parseInt(assetIdGen.getId());
	     	String brancId = assetIdGen.getBranchId();
	     	String deptId = assetIdGen.getDepartmentId();
	     	String catId = assetIdGen.getCategory();
	     	String sectId = assetIdGen.getSection();
	     	int userId = assetIdGen.getUserid();
	     	System.out.println("<<<<<<<<<<<<brancId: "+brancId+"    <<<<deptId: "+deptId+"    <<<<catId: "+catId+"    <<<<sectId: "+sectId);	
			String assetId = new legend.AutoIDSetup().getIdentity(brancId,deptId, sectId, catId);
			assetCode = Integer.parseInt(new ApplicationHelper().getGeneratedId("AM_ASSET"));
		System.out.println("<<<<<<<<<<<<groupId: "+groupId+"    <<<<Id: "+Id+"    <<<<assetId: "+assetId+"    <<<<assetCode: "+assetCode);	
		assetIdandCodeGen(groupId, Id, assetId, assetCode);
		
  	   	 Name =htmlUtil.getCodeName(" SELECT full_name from am_gb_user where user_id='"+userId+"'");
//  	   	 url = "DocumentHelp.jsp?np=groupAssetPosting&amp;id=" + groupId + "&pageDirect=Y";
  	   	
	     htmlUtil.insGrpToAm_Invoice_No(assetId,lpo,invoiceNum,"Asset Creation",groupId);
         page1 = "ASSET GROUP CREATION RAISE ENTRY";
   	     String qryBranch =" SELECT branch_name from am_ad_branch where branch_id='"+brancId+"'";
   	   	 Branch = htmlUtil.getCodeName(qryBranch);
//   	   	String subjectT= adGroup.subjectToVat(group_id);
//   	   	String whT= adGroup.whTax(group_id);
   	   	 Name =htmlUtil.getCodeName(" SELECT full_name from am_gb_user where user_id='"+userId+"'");
   	   	 url = "DocumentHelp.jsp?np=groupAssetPosting&amp;id=" + groupId + "&pageDirect=Y";
	  	
	     }  
	   String  depRate = htmlUtil.getCodeName(" SELECT DEP_RATE FROM AM_GROUP_ASSET WHERE Group_id = '"+groupId+"'");
	     boolean status = saveGroupToAsset(groupId,depRate);
	 	boolean approval_level_val =checkApprovalStatus("3");

			String [] approvalResult=ad.setApprovalDataGroup(Long.parseLong(groupId));
			approvalResult[10]="A";
			System.out.println("<<<<<<<<<<<<status: "+status+"    <<<<approval_level_val: "+approval_level_val);
      	if((status)&&(approval_level_val))
      	{
//      		System.out.println("<<<<<<<<<<<<assetRaiseEntry: "+assetRaiseEntry+"    <<<<groupAssetByAsset: "+groupAssetByAsset);
//			if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
//				aprecords.insertApprovalx2(groupId, approvalResult[5], page1, flag, partPay,Name, Branch, subjectT,
//                  whT, url,Integer.parseInt(approvalResult[0]),assetCode);
//          }
//      	 System.out.println("====== Inserting into Approval ======");
      	 changeGroupAssetStatus(groupId,"PENDING");
//      	if(singleApproval.equalsIgnoreCase("Y")){
//        String trans_id = setGroupPendingTrans(ad.setApprovalDataGroup(Long.parseLong(groupId)),"3",assetCode);
//        
//                 ad.setPendingTransArchive(ad.setApprovalDataGroup(Long.parseLong(groupId)),"3",Integer.parseInt(approvalResult[0]),assetCode);
//      	 //write a method to change status to pending
//    	}
      	if(singleApproval.equalsIgnoreCase("N")){
      		System.out.println("approvelist.size()#$$$$$$$$$$$ "+approvelist.size());
     	   	 for(int i=0;i<approvelist.size();i++)
       	     {  
       		  	legend.admin.objects.User usr = (legend.admin.objects.User)approvelist.get(i);   	 
       			String supervisorId =  usr.getUserId();
       			mailAddress = usr.getEmail();
       			supervisorName = usr.getUserName();
       			String supervisorfullName = usr.getUserFullName();
//       			System.out.println("SupervisorId#$$$$$$$$$$$ "+supervisorId+"    <<<<groupId: "+groupId);
       	  		 String trans_id = setGroupPendingMultipleTrans(ad.setApprovalDataGroup(Long.parseLong(groupId)),"3",assetCode,supervisorId,groupId);
       			 String lastMTID = ad.getCurrentMtid("am_asset_approval");
//       		 ad.setPendingMultiApprTransArchive(ad.setApprovalData(id),"1",Integer.parseInt(lastMTID),assetCode,supervisorId);
       			String subjectr ="Asset Creation Approval";
       			String msgText11 ="Asset with ID: "+ groupId +" is waiting for your approval.";
        		String  approvaltransId  = aprecords.getCodeName("select transaction_id from am_asset_approval where ASSET_ID='"+groupId+"'");	  
       //String otherparam = "newAssetApproval&operation=1&id="+id+"&tranId="+approvaltransId+"&transaction_level=1&approval_level_count=0&assetCode="+assetCode;				
       				 comp.insertMailRecords(mailAddress,subjectr,msgText11);
       	     	}	
      		
      	}
       	if(singleApproval.equalsIgnoreCase("Y")){
	        String trans_id = setGroupPendingTrans(ad.setApprovalDataGroup(Long.parseLong(groupId)),"3",assetCode);
	      	approvalResult=ad.setApprovalDataGroup(Long.parseLong(groupId));
            ad.setPendingTransArchive(ad.setApprovalDataGroup(Long.parseLong(groupId)),"3",Integer.parseInt(approvalResult[0]),assetCode);	      	
       	}
      	}	
   	  	
	     } 
//		System.out.println("value#$$$$$$$$$$$ "+value);
		return value;

	}
    public long [] insertGroupAssetRecordUnclassified(String groupAssetByAsset,String singleApproval,String branch,String departCode,String userName) throws Exception, Throwable {
		//System.out.println("INSIDE INSERT GROUP ASSET RECORD OF GROUP ASSET BEAN");
    	java.util.ArrayList approvelist =ad.getApprovalsId(branch,departCode,userName);
		String[] budget = getBudgetInfo();
		double[] bugdetvalues = getBudgetValues();
		int DONE = 0; // everything is oK
		int BUDGETENFORCED = 1; // EF budget = Yes, CF = NO, ERROR_FLAG
		int BUDGETENFORCEDCF = 2; // EF budget = Yes, CF = Yes, ERROR_FLAG
		int ASSETPURCHASEBD = 3; // asset falls into no quarter purchase date

		long [] value = new long[2];

		// older than bugdet
		String Q = getQuarter();
//		System.out.println("BUDGET VALUE  =======  "  + budget[3]);
		if(budget[3].equalsIgnoreCase("N")){
			value[1]=createGroupUnclassified(branch);
			value[0]= DONE;
		}
		else if(budget[3].equalsIgnoreCase("Y")){
		if (!Q.equalsIgnoreCase("NQ")) {
			if (budget[3].equalsIgnoreCase("Y")
					&& budget[4].equalsIgnoreCase("N")) {
				if (chkBudgetAllocation(Q, bugdetvalues, false)) {
					updateBudget(Q, budget);
					value[1]=createGroupUnclassified(branch);
					value[0]= DONE;
				} else {
					value[0]=  BUDGETENFORCED;
				}

			} else if (budget[3].equalsIgnoreCase("Y")
					&& budget[4].equalsIgnoreCase("Y")) {
				if (chkBudgetAllocation(Q, bugdetvalues, true)) {
					updateBudget(Q, budget);
					value[1]=createGroupUnclassified(branch);
					value[0]=  DONE;
				} else {
					value[0]=  BUDGETENFORCEDCF;
				}

			} else {
				value[1]=createGroupUnclassified(branch);
				value[0]= DONE;
			}
		} else {
			//value[1]=createGroup();
			value[0]= ASSETPURCHASEBD;
		}
	}
	else{}
		
		if(groupAssetByAsset.equalsIgnoreCase("N")){
		String groupId = String.valueOf(value[1]);
		int assetCode = 0;
        String page1 = "";
        String flag= "";
  	  	String partPay="";
	   	String subjectT= "";
	   	String whT= "";
  	   	String Name = "";
  	   	String url = "";
  	   	String assetId = "";
  	   	String mailAddress = "";
  	   	String supervisorName = "";
  //	  	String qryBranch =" SELECT branch_name from am_ad_branch where branch_id='"+brancId+"'";
  	   	String Branch = "";
//  	   	String subjectT= adGroup.subjectToVat(group_id);
//  	   	String whT= adGroup.whTax(group_id);

  	   	
//		System.out.println("<<<<<<<<<<<<catid: "+groupAssetByAsset+"    <<<<groupAssetByAsset: "+groupId);
	     java.util.ArrayList catlist =getUncapAsstCategoryId(groupId);
	     java.util.ArrayList AssetIdGenlist =getUncapAsstIdGeneration(groupId);
	     String assetRaiseEntry =htmlUtil.getCodeName(" SELECT raise_entry from am_gb_company ");
//	     System.out.println("<<<<<<<< Group By Category List Number>> "+catlist.size() );
//	     System.out.println("<<<<<<<< Asset By Id List Number>> "+AssetIdGenlist.size() );
//	     boolean duplicate = comp.raisentry_postDuplicate();
//	     boolean duplicate2 = comp.asset_approvalDuplicate();
	      //================================
	     for(int i=0;i<catlist.size();i++)
	     {
	     com.magbel.legend.vao.ViewAssetDetails  assetcat = (com.magbel.legend.vao.ViewAssetDetails)catlist.get(i);    	 
			int catid =  assetcat.getId();
			double depRate = assetcat.getDepRate();
//		System.out.println("<<<<<<<<<<<<catid: "+catid+"    <<<<depRate: "+depRate);	
			groupUncpAssetRateGen(groupId,catid,depRate);
	     }    
	     for(int i=0;i<AssetIdGenlist.size();i++)
	     {
	    	 magma.net.vao.Asset  assetIdGen = (magma.net.vao.Asset)AssetIdGenlist.get(i);    
	     	int Id = Integer.parseInt(assetIdGen.getId());
	     	String brancId = assetIdGen.getBranchId();
	     	String deptId = assetIdGen.getDepartmentId();
	     	String catId = assetIdGen.getCategory();
	     	String sectId = assetIdGen.getSection();
	     	int userId = assetIdGen.getUserid();
//	     	System.out.println("<<<<<<<<<<<<brancId: "+brancId+"    <<<<deptId: "+deptId+"    <<<<catId: "+catId+"    <<<<sectId: "+sectId+"   <<<userId: "+userId);	
			assetId =  new legend.AutoIDSetup().getIdentity(brancId,deptId, sectId, catId);
			assetCode = Integer.parseInt(new ApplicationHelper().getGeneratedId("AM_ASSET_UNCAPITALIZED"));
//		System.out.println("<<<<<<<<<<<<groupId: "+groupId+"    <<<<Id: "+Id+"    <<<<assetId: "+assetId+"    <<<<assetCode: "+assetCode);	
			UncapAssetIdandCodeGen(groupId, Id, assetId, assetCode);
		
  	   	 Name =htmlUtil.getCodeName(" SELECT full_name from am_gb_user where user_id='"+userId+"'");
//  	   	 url = "DocumentHelp.jsp?np=groupAssetPostingBranch&amp;id=" + groupId + "&pageDirect=Y";
  	   	
	     htmlUtil.insGrpToAm_Invoice_No(assetId,lpo,invoiceNum,"Uncapitalised Asset Creation",groupId);
         page1 = "ASSET GROUP CREATION RAISE ENTRY";
   	     String qryBranch =" SELECT branch_name from am_ad_branch where branch_id='"+brancId+"'";
   	   	 Branch = htmlUtil.getCodeName(qryBranch);
//   	   	String subjectT= adGroup.subjectToVat(group_id);
//   	   	String whT= adGroup.whTax(group_id);
   	   	 Name =htmlUtil.getCodeName(" SELECT full_name from am_gb_user where user_id='"+userId+"'");
   	   	 url = "DocumentHelp.jsp?np=groupAssetPostingBranch&amp;id=" + groupId + "&pageDirect=Y";
	  	 
	     }  
	     boolean status = saveGroupToUncapAsset(groupId);
  	   	boolean approval_level_val =checkApprovalStatus("19");
  	  	
			String [] approvalResult=ad.setApprovalDataGroup(Long.parseLong(groupId));
			approvalResult[10]="A";

//			System.out.println("<<<<<<<<<<<<status: "+status+"    <<<<approval_level_val: "+approval_level_val);			
      	if((status)&&(approval_level_val))
      	{
//			if(assetRaiseEntry != null && assetRaiseEntry.equalsIgnoreCase("Y")){
//				aprecords.insertApprovalx2(groupId, approvalResult[5], page1, flag, partPay,Name, Branch, subjectT,
//                  whT, url,Integer.parseInt(approvalResult[0]),assetCode);
//          }
//      	 System.out.println("====== Inserting into Approval ======");
      	 changeGroupAssetStatus(groupId,"PENDING");
//        String trans_id = setGroupPendingTrans(ad.setApprovalDataGroup(Long.parseLong(groupId)),"19",assetCode);
        
//                 ad.setPendingTransArchive(ad.setApprovalDataGroup(Long.parseLong(groupId)),"19",Integer.parseInt(approvalResult[0]),assetCode);
      	 //write a method to change status to pending
                 
               	if(singleApproval.equalsIgnoreCase("N")){
//          		System.out.println("approvelist.size()#$$$$$$$$$$$ "+approvelist.size());
         	   	 for(int i=0;i<approvelist.size();i++)
           	     {  
           		  	legend.admin.objects.User usr = (legend.admin.objects.User)approvelist.get(i);   	 
           			String supervisorId =  usr.getUserId();
           			mailAddress = usr.getEmail();
           			supervisorName = usr.getUserName();
           			String supervisorfullName = usr.getUserFullName();
//           			System.out.println("SupervisorId#$$$$$$$$$$$ "+supervisorId+"    <<<<groupId: "+groupId);
           	  		 String trans_id = setGroupPendingMultipleTrans(ad.setApprovalDataGroup(Long.parseLong(groupId)),"19",assetCode,supervisorId,groupId);
           			 String lastMTID = ad.getCurrentMtid("am_asset_approval");
//           		 ad.setPendingMultiApprTransArchive(ad.setApprovalData(id),"1",Integer.parseInt(lastMTID),assetCode,supervisorId);
           			String subjectr ="Asset Creation Approval";
           			String msgText11 ="Asset with ID: "+ groupId +" is waiting for your approval.";
            		String  approvaltransId  = aprecords.getCodeName("select transaction_id from am_asset_approval where ASSET_ID='"+groupId+"'");	  
           //String otherparam = "newAssetApproval&operation=1&id="+id+"&tranId="+approvaltransId+"&transaction_level=1&approval_level_count=0&assetCode="+assetCode;				
           				 comp.insertMailRecords(mailAddress,subjectr,msgText11);
           	     	}	
          		
          	}
           	if(singleApproval.equalsIgnoreCase("Y")){
    	        String trans_id = setGroupPendingTrans(ad.setApprovalDataGroup(Long.parseLong(groupId)),"19",assetCode);
    	      	approvalResult=ad.setApprovalDataGroup(Long.parseLong(groupId));
                ad.setPendingTransArchive(ad.setApprovalDataGroup(Long.parseLong(groupId)),"19",Integer.parseInt(approvalResult[0]),assetCode);	      	
           	}
                 
                 
      	}	
   	  	
	     } 
		
		return value;
	}

	public String[] getBudgetInfo() {
		String[] result = new String[5];

		String query = " SELECT financial_start_date,financial_no_ofmonths"
				+ ",financial_end_date,enforce_acq_budget,quarterly_surplus_cf"
				+ " FROM am_gb_company";

		
		try {
			Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				result[0] = sdf.format(rs.getDate("financial_start_date"));
				result[1] = rs.getString("financial_no_ofmonths");
				result[2] = sdf.format(rs.getDate("financial_end_date"));
				result[3] = rs.getString("enforce_acq_budget");
				result[4] = rs.getString("quarterly_surplus_cf");

			}
		} catch (Exception e) {
			String warning = "WARNING:Error Fetching Company Details in getBudgetInfo" + " ->"
					+ e.getMessage();
			System.out.println(warning);
		} 

		return result;
	}

	public double[] getBudgetValues() {



		double[] result = new double[8];

		String query = " SELECT [Q1_ALLOCATION],[Q1_ACTUAL],[Q2_ALLOCATION]"
				+ ",[Q2_ACTUAL],[Q3_ALLOCATION],[Q3_ACTUAL],[Q4_ALLOCATION],[Q4_ACTUAL],BALANCE_ALLOCATION,TOTAL_ACTUAL"
				+ " FROM [AM_ACQUISITION_BUDGET] WHERE [CATEGORY_CODE]='"
				+ getCatCode() + "' AND " + " [BRANCH_ID]='" + branch_id + "'";
		 System.out.println("=====>>>>query in getBudgetValues: "+query);
		
		try {
			Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				result[0] = rs.getDouble("Q1_ALLOCATION");
				result[1] = rs.getDouble("Q1_ACTUAL");
				result[2] = rs.getDouble("Q2_ALLOCATION");
				result[3] = rs.getDouble("Q2_ACTUAL");
				result[4] = rs.getDouble("Q3_ALLOCATION");
				result[5] = rs.getDouble("Q3_ACTUAL");
				result[6] = rs.getDouble("Q4_ALLOCATION");
				result[7] = rs.getDouble("Q4_ACTUAL");
                result[8] = rs.getDouble("BALANCE_ALLOCATION");
                result[9] = rs.getDouble("TOTAL_ACTUAL");				
				// result[4] = rs.getDouble("quarterly_surplus_cf");

			}
		} catch (Exception e) {
			String warning = "WARNING:Error Fetching Company Details IN getBudgetValues" + " ->"
					+ e.getMessage();
			System.out.println(warning);
		} 
//		System.out.println("=====>>>>result[3] in getBudgetValues: "+result[3]);
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
//    	dbConnection = new MagmaDBConnection();
    	
		String query = "SELECT CATEGORY_CODE  FROM am_ad_category  "
				+ "WHERE category_id = '" + category_id + "' ";
		String catid = "0";
		try {
			Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
			 {
				catid = rs.getString(1);
			 }
		} catch (Exception ex) {
			ex.printStackTrace();
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

	public void updateBudgetOld(String quarter, String[] bugdetinfo) throws SQLException {

		String fisdate = "";
		int finomonth = 0;
		String fiedate = "";
		Connection conn = dbConnection.getConnection("legendPlus");
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

				//System.out.println(budgetUpdate1);

				stmt.executeUpdate(budgetUpdate1);

				//System.out.println("Updated 3rd Quarter");
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
	
	public void updateBudget(String quarter, String[] budgetInfo) {

	    String columnName;

	    switch (quarter.toUpperCase()) {
	        case "FIRST":
	            columnName = "Q1_ACTUAL";
	            break;
	        case "2ND":
	            columnName = "Q2_ACTUAL";
	            break;
	        case "3RD":
	            columnName = "Q3_ACTUAL";
	            break;
	        case "4TH":
	            columnName = "Q4_ACTUAL";
	            break;
	        default:
	            throw new IllegalArgumentException("Invalid quarter: " + quarter);
	    }

	    String updateSql =
	        "UPDATE AM_ACQUISITION_BUDGET " +
	        "SET " + columnName + " = (" + columnName + " + ?) " +
	        "WHERE BRANCH_ID = ? " +
	        "AND CATEGORY = ? " +
	        "AND ACC_START_DATE = ? " +
	        "AND ACC_END_DATE = ?";

	    try (Connection conn = dbConnection.getConnection("legendPlus");
	         PreparedStatement ps = conn.prepareStatement(updateSql)) {

	        double cost = Double.parseDouble(
	                vatable_cost.replace(",", "")
	        );

	        ps.setDouble(1, cost);
	        ps.setString(2, branch_id);
	        ps.setString(3, getCatCode());
	        ps.setDate(4, dateFormat.dateConvert(budgetInfo[0]));
	        ps.setDate(5, dateFormat.dateConvert(budgetInfo[2]));

	        ps.executeUpdate();

	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }

	    System.out.println("Budget updated successfully.");
	}

	private boolean rinsertAssetRecord() throws Exception, Throwable {
		
		boolean done = true;
		/*
		 * if (require_redistribution.equalsIgnoreCase("Y")) { status = "Z"; }
		 */
		Codes code = new Codes();
		if (make == null || make.equals("")) {
			make = "0";
		}
		if (maintained_by == null || maintained_by.equals("")) {
			maintained_by = "0";
		}
		if (supplied_by == null || supplied_by.equals("")) {
			supplied_by = "0";
		}
		if (user == null || user.equals("")) {
			user = "";
		}
		if (location == null || location.equals("")) {
			location = "0";
		}
		if (driver == null || driver.equals("")) {
			driver = "0";
		}
		if (state == null || state.equals("")) {
			state = "0";
		}
		if (department_id == null || department_id.equals("")) {
			department_id = "0";
		}
		if (vat_amount == null || vat_amount.equals("")) {
			vat_amount = "0.0";
		}
		if (vatable_cost == null || vatable_cost.equals("")) {
			vatable_cost = "0.0";
		}
		if (wh_tax_amount == null || wh_tax_amount.equals("")) {
			wh_tax_amount = "0";
		}
		if (branch_id == null || branch_id.equals("")) {
			branch_id = "0";
		}
		if (province == null || province.equals("")) {
			province = "0";
		}
		if (category_id == null || category_id.equals("")) {
			category_id = "0";
		}

		if (residual_value == null || residual_value.equals("")) {
			residual_value = "0";
		}
		if (section_id == null || section_id.equals("")) {
			section_id = "0";
		}

		if (noOfMonths == null || noOfMonths.equals("")) {
			noOfMonths = "0";
		}
		if (warrantyStartDate == null || warrantyStartDate.equals("")) {
			warrantyStartDate = null;
		}
		if (expiryDate == null || expiryDate.equals("")) {
			expiryDate = null;
		}

		vat_amount = vat_amount.replaceAll(",", "");
		vatable_cost = vatable_cost.replaceAll(",", "");
		wh_tax_amount = wh_tax_amount.replaceAll(",", "");
		residual_value = residual_value.replaceAll(",", "");

		String createQuery = "INSERT INTO AM_GROUP_ASSET("
				+ "REGISTRATION_NO,BRANCH_ID,DEPT_ID,"
				+ "CATEGORY_ID,SECTION_ID,DESCRIPTION,"
				+ "VENDOR_AC,DATE_PURCHASED,DEP_RATE,"
				+ "ASSET_MAKE,ASSET_MODEL,ASSET_SERIAL_NO,"
				+ "ASSET_ENGINE_NO,SUPPLIER_NAME,"
				+ "ASSET_USER,ASSET_MAINTENANCE,"
				+ "COST_PRICE,DEP_END_DATE,RESIDUAL_VALUE,"
				+ "AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,"
				+ "POSTING_DATE,EFFECTIVE_DATE,"
				+ "PURCHASE_REASON,LOCATION,VATABLE_COST,"
				+ "VAT,REQ_DEPRECIATION,SUBJECT_TO_VAT,"
				+ "USER_ID,BRANCH_CODE,DEPT_CODE,"
				+ "SECTION_CODE,CATEGORY_CODE,defer_pay, sub_category_id,SUB_CATEGORY_CODE,TRANPORT_COST,OTHER_COST) "
				+ " VALUES"
				+ " (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?,?)";
		try {
			double costPrice = Double.parseDouble(vat_amount)
					+ Double.parseDouble(vatable_cost);
			Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(createQuery);
			ps.setString(1, registration_no);
			ps.setInt(2, Integer.parseInt(branch_id));
			ps.setInt(3, Integer.parseInt(department_id));
			ps.setInt(4, Integer.parseInt(category_id));
			ps.setInt(5, Integer.parseInt(section_id));
			ps.setString(6, description.toUpperCase());
			ps.setString(7, vendor_account);
			ps.setString(8, DateManipulations
					.CalendarToDb(date_of_purchase));
			ps.setString(9, getDepreciationRate(category_id));
			ps.setString(10, make);
			ps.setString(11, model);
			ps.setString(12, serial_number);
			ps.setString(13, engine_number);
			ps.setInt(14, Integer.parseInt(supplied_by));
			ps.setString(15, user);
			ps.setInt(16, Integer.parseInt(maintained_by));
			ps.setDouble(17, costPrice);
			ps.setDate(18, dbConnection.dateConvert(depreciation_end_date));
			ps.setDouble(19, Double.parseDouble(residual_value));
			ps.setString(20, authorized_by);
			ps.setString(21, wh_tax_cb);
			ps.setDouble(22, Double.parseDouble(wh_tax_amount));
			ps.setTimestamp(23, dbConnection.getDateTime(new java.util.Date()));
			ps.setDate(24, dbConnection.dateConvert(depreciation_start_date));
			ps.setString(25, reason);
			ps.setString(26, "");
			ps.setDouble(27, Double.parseDouble(vatable_cost));
			ps.setDouble(28, Double.parseDouble(vat_amount));
			ps.setString(29, require_depreciation);
			ps.setString(30, subject_to_vat);
			ps.setString(31, user_id);
			ps.setString(32, code.getBranchCode(branch_id));
			ps.setString(33, code.getDeptCode(department_id));
			ps.setString(34, code.getSectionCode(section_id));
			ps.setString(35, code.getCategoryCode(category_id));
			ps.setString(36, deferPay);
			ps.setString(37, category_id);
			ps.setString(38,code.getCategoryCode(category_id));   
			ps.setDouble(39, Double.parseDouble(transport_cost));
			ps.setDouble(40, Double.parseDouble(other_cost));
			ps.execute();

		} catch (Exception ex) {
			done = false;
			System.out.println("WARN:Error creating asset->" + ex);
		} 

		return done;
	}

	/**
	 * getDepreciationRate
	 *
	 * @param category_id
	 *            String
	 * @return String
	 */
	private String getDepreciationRate(String category_id) throws Exception {

		

		String rate = "0.0";
		String query = "SELECT DEP_RATE FROM AM_AD_CATEGORY "
				+ "WHERE CATEGORY_ID = " + category_id;
		try {
			Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				rate = rs.getString(1);
			}

		} catch (Exception ex) {
			System.out.println("WARN: Error fetching DepreciationRate ->" + ex);
		} 

		return rate;
	}

	public String computeTotalLife(String depRate) {

		String totalLife = "0";
		if (depRate == null || depRate.equals("")) {
			depRate = "0.0";
		}

		double division = 100 / (Double.parseDouble(depRate));
		int intTotal = (int) (division * 12);

		totalLife = Integer.toString(intTotal);

		return totalLife;

	}

	public int insertAssetRecord() throws Exception, Throwable {
		String[] budget = getBudgetInfo();
		double[] bugdetvalues = getBudgetValues();
		int DONE = 0; // everything is oK
		int BUDGETENFORCED = 1; // EF budget = Yes, CF = NO, ERROR_FLAG
		int BUDGETENFORCEDCF = 2; // EF budget = Yes, CF = Yes, ERROR_FLAG
		int ASSETPURCHASEBD = 3; // asset falls into no quarter purchase date
									// older than bugdet
		String Q = getQuarter();
		if(budget[3].equalsIgnoreCase("N")){
			rinsertAssetRecord();
			return DONE;
		}
		else if(budget[3].equalsIgnoreCase("Y")){
		if (!Q.equalsIgnoreCase("NQ")) {
			if (budget[3].equalsIgnoreCase("Y")
					&& budget[4].equalsIgnoreCase("N")) {
				if (chkBudgetAllocation(Q, bugdetvalues, false)) {
					updateBudget(Q, budget);
					rinsertAssetRecord();
					return DONE;
				} else {
					return BUDGETENFORCED;
				}

			} else if (budget[3].equalsIgnoreCase("Y")
					&& budget[4].equalsIgnoreCase("Y")) {
				if (chkBudgetAllocation(Q, bugdetvalues, true)) {
					updateBudget(Q, budget);
					rinsertAssetRecord();
					return DONE;
				} else {
					return BUDGETENFORCEDCF;
				}

			} else {
				rinsertAssetRecord();
				return DONE;
			}
		} else {
			// rinsertAssetRecord();
			return ASSETPURCHASEBD;
		}

		}

		 return 0;
	}

	/**
	 * @return the raise_entry
	 */
		/**
	 * @return the amountPTD
	 */
	public String getAmountPTD() {
		return amountPTD;
	}

	/**
	 * @param amountPTD the amountPTD to set
	 */
	public void setAmountPTD(String amountPTD) {
		this.amountPTD = amountPTD;
	}

	/**
	 * @return the amountREM
	 */
	public String getAmountREM() {
		return amountREM;
	}

	/**
	 * @param amountREM the amountREM to set
	 */
	public void setAmountREM(String amountREM) {
		this.amountREM = amountREM;
	}

	/**
	 * @return the fullyPAID
	 */
	public String getFullyPAID() {
		return fullyPAID;
	}

	/**
	 * @param fullyPAID the fullyPAID to set
	 */
	public void setFullyPAID(String fullyPAID) {
		this.fullyPAID = fullyPAID;
	}

	/**
	 * @param expiryDate the expiryDate to set
	 */
	 public void setExpiryDate(String expiryDate) {
	        if (expiryDate != null) {
	            this.expiryDate = expiryDate;
	        }
	    }

	/**
	 * @param warrantyStartDate the warrantyStartDate to set
	 */
	/**
	 * @return the partPAY
	 */
	public String getPartPAY() {
		return partPAY;
	}

	/**
	 * @param partPAY the partPAY to set
	 */
	public void setPartPAY(String partPAY) {
		this.partPAY = partPAY;
	}

    /**
     * @return the asset_status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param asset_status the asset_status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the supervisor
     */
    public String getSupervisor() {
        return supervisor;
    }

    /**
     * @param supervisor the supervisor to set
     */
    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public boolean isMultipleComponent(String category_id) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        boolean multipleComponent = false;
        String query = "SELECT COUNT(AM_ID) FROM AM_CT_COMPONENT  " +
                       "WHERE CATEGORY = " + category_id;
        try {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                int numRecords = rs.getInt(1);
                if (numRecords > 0) {
                    multipleComponent = true;
                }
            }

        } catch (Exception ex) {
            System.out.println("WARN: Error determining  MultipleComponent->" +
                               ex);
        } finally {
            dbConnection.closeConnection(con, ps);
        }

        return multipleComponent;
    }

    public long getGroupID(String aid) {

        
        long numRecords=0;
        //boolean multipleComponent = false;
        String query = "SELECT distinct group_id FROM am_group_asset where asset_id='"+aid +"'";
        /*System.out.println("<<<<<<<< Inside getGrtoupID >>>>>>>>");
        System.out.println("Query >>>> " + query);*/
        try {
        	Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                numRecords = rs.getLong(1);
               // System.out.println("Group_ID >>>> " + numRecords);
            }

        } catch (Exception ex) {
            System.out.println("GroupAssetBean: getGroupID(): WARN: Error determining  group id->" +
                               ex);
        } 

        return numRecords;
    }

    public long getGroupUncapID(String aid) {

        
        long numRecords=0;
        //boolean multipleComponent = false;
        String query = "SELECT distinct group_id FROM AM_GROUP_ASSET_UNCAPITALIZED where asset_id='"+aid +"'";
        /*System.out.println("<<<<<<<< Inside getGrtoupID >>>>>>>>");
        System.out.println("Query >>>> " + query);*/
        try {
        	Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next())
            {
                numRecords = rs.getLong(1);
               // System.out.println("Group_ID >>>> " + numRecords);
            }

        } catch (Exception ex) {
            System.out.println("GroupAssetBean: getGroupUncapID(): WARN: Error determining  group id->" +
                               ex);
        } 

        return numRecords;
    }    
    public String subjectToVat(String id){
    	String result="";
    	   

    	         String query =
    	                "SELECT Subject_TO_Vat FROM am_group_asset_main  " +
    	                "WHERE group_id = '" + id + "' ";
    	        //System.out.println("Query in Subject To Vat : " + query);

    	        try {
    	        	Connection con = dbConnection.getConnection("legendPlus");
    	             PreparedStatement ps = con.prepareStatement(query);
    	            ResultSet rs = ps.executeQuery();
    	            while (rs.next()) {
    	                result = rs.getString(1);
    	            }

    	        } catch (Exception ex) {
    	            System.out.println("WARN: Error fetching Subject_TO_Vat ->" + ex);
    	        } 

    	        return result;
    	}
    public String whTax(String id){
    	String result="";
    	   

    	         String query =
    	                "SELECT wh_tax FROM am_group_asset_main  " +
    	                "WHERE group_id = '" + id + "' ";

    	         //System.out.println("Query in whTax : " + query);
    	        try {
    	        	Connection con = dbConnection.getConnection("legendPlus");
    	             PreparedStatement ps = con.prepareStatement(query);
    	            ResultSet rs = ps.executeQuery();
    	            while (rs.next()) {
    	                result = rs.getString(1);
    	            }

    	        } catch (Exception ex) {
    	            System.out.println("WARN: Error fetching WHTAX ->" + ex);
    	        } 

    	        return result;
    	}
    public void updateGroupAssetRaiseEntry(String group_id) {

	      
	       String NOTIFY_QUERY ="UPDATE am_group_asset_main SET raise_entry = ? WHERE Group_id = ?  ";

	       try {
	    	   Connection con = dbConnection.getConnection("legendPlus");
	             PreparedStatement ps = con.prepareStatement(NOTIFY_QUERY);
	           ps.setString(1, "Y");
	           ps.setString(2, group_id);
	           int result =  ps.executeUpdate();
	           //System.out.println("The Result Of the Update :::::::::: " + result);
	           updateGroupAssetMainRaiseEntry(group_id);

	       } catch (Exception ex) {
	           System.out.println("WARNING: cannot update am_group_asset_main : "+ex.getMessage());
	       } 
	   }

    public void updateGroupAssetMainRaiseEntry(String group_id) {

	       
	       String NOTIFY_QUERY ="UPDATE am_group_asset SET raise_entry = ? WHERE Group_id = ?  ";

	       try {
	    	   Connection con = dbConnection.getConnection("legendPlus");
	           PreparedStatement ps = con.prepareStatement(NOTIFY_QUERY);
	           ps.setString(1, "Y");
	           ps.setString(2, group_id);
	           ps.executeUpdate();

	       } catch (Exception ex) {
	           System.out.println("WARNING: cannot update am_group_asset : "+ex.getMessage());
	       } 

	   }

    public String setGroupPendingTrans(String[] a, String code){

        int transaction_level=0;
        
 String pq =
	 "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description," +
	 "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time," +
	 "transaction_id,batch_id,transaction_level) " +
	 "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 String tranLevelQuery = "select level from approval_level_setup where code ='"+code+"'";
        
        try
        {
        	Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(tranLevelQuery);
            ResultSet  rs = ps.executeQuery();
            while(rs.next()){
            transaction_level = rs.getInt(1);
            }
            ps = con.prepareStatement(pq);

            SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

            mtid =  new ApplicationHelper().getGeneratedId("am_asset_approval");

            ps.setString(1, (a[0]==null)?"":a[0]);
            ps.setString(2, (a[1]==null)?"":a[1]);
            ps.setString(3, (a[2]==null)?"":a[2]);
            ps.setDouble(4, (a[3]==null)?0:Double.parseDouble(a[3]));
            //System.out.println("Posting_Date b4 conversion in setPendingTrans : " +  a[4]);
            ps.setDate(5, (a[4])==null?null:dbConnection.dateConvert(a[4]));
            //System.out.println("Posting_Date after setPendingTrans : " +  a[4]);
            ps.setString(6, (a[5]==null)?"":a[5]);
           // System.out.println("effective_date b4 conversion in setPendingTrans : " +  a[6]);
            ps.setDate(7,(a[6])==null?null:dbConnection.dateConvert(a[6]));
            //System.out.println("effective_date after conversion in setPendingTrans : " +  a[6]);
            ps.setString(8, (a[7]==null)?"":a[7]);
            ps.setString(9, (a[8]==null)?"":a[8]); //asset_status
            ps.setString(10, (a[9]==null)?"":a[9]);
            ps.setString(11, a[10]);
            ps.setString(12, timer.format(new java.util.Date()));
            ps.setString(13,mtid);
            ps.setString(14, mtid);
            ps.setInt(15, transaction_level);

            ps.execute();

        }
        catch(Exception er)
        {
            System.out.println(">>>GroupAssetBean:setGroupPendingTrans(>>>>>>" + er);

        }
        
        return mtid;
    }

    public long [] updateGroupAssetRecord(String gid) throws Exception, Throwable
	{
    	//System.out.println("GROUP ID FROM JSP :::: " + gid);
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


    private long updateCreatedGroup(String gid)
	{
    	String update_am_group_asset_main_qry =
    		"UPDATE am_group_asset_main  SET [quantity] = ?," +
    		"[Registration_No] = ?," +"[Branch_ID] = ? "+",[Dept_ID] = ?," +"[Category_ID] = ?," +
    		"[section_id] = ?," +"[Description] = ? ," +"[Vendor_AC] =? " +",[Date_purchased] = ?," +
    		"[dep_rate] = ?," + "[asset_make] = ?," + "[asset_model] = ?," + "[asset_serial_no] =? " +
    	        ",[asset_engine_no] = ?," +"[supplier_name] = ?," + "[asset_user] = ?," +"[asset_maintenance] = ? "+
    		",[Cost_Price] = ?," + "[dep_end_date] = ?," +"[residual_value] = ?," + "[authorized_by] = ?," +
    		"[wh_tax] = ? " + ",[wh_tax_amount] = ?," + "[req_redistribution] = ?," +"[Posting_Date] = ?," +
    		"[effective_date] = ?"+ ",[purchase_reason] = ?," +  "[location] = ?," + "[Vatable_Cost] =?," +
    		"[Vat] = ?," +"[Req_Depreciation] = ?"+ ",[Subject_TO_Vat] = ?," +"[Who_TO_Rem] = ?," +
    		"[email1] = ?," + "[who_to_rem_2] = ?," +"[email2] =?"+ ",[dep_ytd] = ?," + "[Section] = ?," +
    		"[raise_entry] = ?," + "[state] = ?," +"[driver] = ?"+ ",[spare_1] =?," + "[spare_2] = ?," +
    		"[user_ID] = ?," + "[province] =?," + "[WAR_START_DATE] =?," + "[WAR_MONTH] = ?"+ ",[WAR_EXPIRY_DATE] = ?," +
    	        "[branch_code] = ?," + "[dept_code] = ?," + "[section_code] = ?," + "[category_code] = ?"+",[AMOUNT_PTD] =?," +
    		"[AMOUNT_REM] = ?," + "[PART_PAY] =?," + "[FULLY_PAID] = ?," + "[supervisor] = ?," + "[BAR_CODE] =?"+
    		",[LPO] =?," + "[ACCUM_DEP] = ?," + "[defer_pay] = ?," + "[process_flag] = ?," + "[SBU_CODE] =?"+
    		",[pend_GrpAssets] =? " + ",[Vatable_Cost_Bal] =? " +",[invoice_no]=?"+",[workstationip]=?  "+
    		  ",Sub_Category_ID=?,sub_category_code=?,spare_3=?,spare_4=?,spare_5=?,spare_6=?,TRANPORT_COST=?,OTHER_COST=? WHERE group_id = "+gid+"";
    	//ADD RAISE ENTRY

        String update_am_group_asset_main_archive_qry =
    		"UPDATE am_group_asset_main_archive  SET [quantity] = ?," +
    		"[Registration_No] = ?," +"[Branch_ID] = ? "+",[Dept_ID] = ?," +"[Category_ID] = ?," +
    		"[section_id] = ?," +"[Description] = ? ," +"[Vendor_AC] =? " +",[Date_purchased] = ?," +
    		"[dep_rate] = ?," + "[asset_make] = ?," + "[asset_model] = ?," + "[asset_serial_no] =? " +
    	        ",[asset_engine_no] = ?," +"[supplier_name] = ?," + "[asset_user] = ?," +"[asset_maintenance] = ? "+
    		",[Cost_Price] = ?," + "[dep_end_date] = ?," +"[residual_value] = ?," + "[authorized_by] = ?," +
    		"[wh_tax] = ? " + ",[wh_tax_amount] = ?," + "[req_redistribution] = ?," +"[Posting_Date] = ?," +
    		"[effective_date] = ?"+ ",[purchase_reason] = ?," +  "[location] = ?," + "[Vatable_Cost] =?," +
    		"[Vat] = ?," +"[Req_Depreciation] = ?"+ ",[Subject_TO_Vat] = ?," +"[Who_TO_Rem] = ?," +
    		"[email1] = ?," + "[who_to_rem_2] = ?," +"[email2] =?"+ ",[dep_ytd] = ?," + "[Section] = ?," +
    		"[raise_entry] = ?," + "[state] = ?," +"[driver] = ?"+ ",[spare_1] =?," + "[spare_2] = ?," +
    		"[user_ID] = ?," + "[province] =?," + "[WAR_START_DATE] =?," + "[WAR_MONTH] = ?"+ ",[WAR_EXPIRY_DATE] = ?," +
    	        "[branch_code] = ?," + "[dept_code] = ?," + "[section_code] = ?," + "[category_code] = ?"+",[AMOUNT_PTD] =?," +
    		"[AMOUNT_REM] = ?," + "[PART_PAY] =?," + "[FULLY_PAID] = ?," + "[supervisor] = ?," + "[BAR_CODE] =?"+
    		",[LPO] =?," + "[ACCUM_DEP] = ?," + "[defer_pay] = ?," + "[process_flag] = ?," + "[SBU_CODE] =?"+
    		",[pend_GrpAssets] =? " + ",[Vatable_Cost_Bal] =? " +",[invoice_no]=?"+",[workstationip]=?"+
    		 ",Sub_Category_ID=?,sub_category_code=?,spare_3=?,spare_4=?,spare_5=?,spare_6=?,TRANPORT_COST=?,OTHER_COST=?  WHERE group_id ="+gid+"";
    	Codes code = new Codes();
    	int itemsCount = Integer.parseInt(no_of_items);
    	if(supervisor==""||supervisor=="0")
		{
			supervisor= user_id;
		}
    	// System.out.println("update_am_group_asset_main_qry >>>> " + update_am_group_asset_main_qry);
         //System.out.println("update_am_group_asset_main_archive_qry >>>> " + update_am_group_asset_main_archive_qry);
		
        try
	    {
        	/*System.out.println("Description >>>>>>>>>> " + description);
        	System.out.println("serial_number >>>>>>>>>> " + serial_number);
        	System.out.println("who_to_rem >>>>>>>>>> " + who_to_rem);
        	System.out.println("email_1 >>>>>>>>>> " + email_1);
                 * */
        	System.out.println("group_id >>>>>>>>>> " + gid);
        	amountPTD = amountPTD.replaceAll(",","");
        	//System.out.println("amountPTD >>>>>>>>>> " + amountPTD);
        	Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(update_am_group_asset_main_qry);
            ps.setInt(1, itemsCount);
            ps.setString(2, registration_no);
            ps.setInt(3, Integer.parseInt(branch_id));
            ps.setInt(4, Integer.parseInt(department_id));
            ps.setInt(5, Integer.parseInt(category_id));
            ps.setInt(6, Integer.parseInt(section_id));
            ps.setString(7, description.toUpperCase());
            ps.setString(8, vendor_account);
            ps.setString(9, DateManipulations.CalendarToDb(date_of_purchase));
            ps.setString(10, getDepreciationRate(category_id));
            ps.setString(11, make);
            ps.setString(12, model);
            ps.setString(13, serial_number);
            ps.setString(14, engine_number);
            ps.setInt(15, Integer.parseInt(supplied_by));
            ps.setString(16, authuser);
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
            ps.setString(33, who_to_rem);
            ps.setString(34, email_1);
            ps.setString(35, who_to_rem_2);
            ps.setString(36, email2);
            ps.setString(37, "0");
            ps.setString(38, section);
            ps.setString(39, "N");
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
            ps.setDouble(60, accum_dep);
            ps.setString(61,deferPay);
            ps.setString(62,"N");
            ps.setString(63, sbu_code);
            ps.setInt(64, itemsCount);
            ps.setDouble(65, Double.parseDouble(vatable_cost));
            ps.setString(66,this.invoiceNum);
            ps.setString(67, this.workstationIp);
            ps.setInt(68, Integer.parseInt(sub_category_id));
            ps.setString(69,code.getSubCategoryCode(sub_category_id));
            ps.setString(70, spare_3);
            ps.setString(71, spare_4);
            ps.setString(72, spare_5);
            ps.setString(73, spare_6);
            ps.setDouble(74, Double.parseDouble(transport_cost));
            ps.setDouble(75, Double.parseDouble(other_cost));
            //ps.setString(68, gid);

           // System.out.println("<<<<<< About to Update am_group_asset_main >>>>>>>>" );
            int test=  ps.executeUpdate();
           // System.out.println("<<<<<< Updated am_group_asset_main >>>>>>>>" + test );

           // System.out.println("<<<<<<  costPrice_flag >>>>>>>>" + costPrice_flag );
            //if cost is different
            ps = con.prepareStatement(update_am_group_asset_main_archive_qry);
            ps.setInt(1, itemsCount);
            ps.setString(2, registration_no);
            ps.setInt(3, Integer.parseInt(branch_id));
            ps.setInt(4, Integer.parseInt(department_id));
            ps.setInt(5, Integer.parseInt(category_id));
            ps.setInt(6, Integer.parseInt(section_id));
            ps.setString(7, description.toUpperCase());
            ps.setString(8, vendor_account);
            ps.setString(9, DateManipulations.CalendarToDb(date_of_purchase));
            ps.setString(10, getDepreciationRate(category_id));
            ps.setString(11, make);
            ps.setString(12, model);
            ps.setString(13, serial_number);
            ps.setString(14, engine_number);
            ps.setInt(15, Integer.parseInt(supplied_by));
            ps.setString(16, authuser);
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
            ps.setString(33, who_to_rem);
            ps.setString(34, email_1);
            ps.setString(35, who_to_rem_2);
            ps.setString(36, email2);
            ps.setString(37, "0");
            ps.setString(38, section);
            ps.setString(39, "N");
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
            ps.setDouble(60, accum_dep);
            ps.setString(61,deferPay);
            ps.setString(62,"N");
            ps.setString(63, sbu_code);
            ps.setInt(64, itemsCount);
            ps.setDouble(65, Double.parseDouble(vatable_cost));
            ps.setString(66,this.invoiceNum);
            ps.setString(67, this.workstationIp);
            ps.setInt(68, Integer.parseInt(sub_category_id));
            ps.setString(69,code.getSubCategoryCode(sub_category_id));
            ps.setString(70, spare_3);
            ps.setString(71, spare_4);
            ps.setString(72, spare_5);
            ps.setString(73, spare_6);    
            ps.setDouble(74, Double.parseDouble(transport_cost));
            ps.setDouble(75, Double.parseDouble(other_cost));
           // ps.setString(68, gid);
          // System.out.println("<<<<<< About to Update am_group_asset_main_archive >>>>>>>>" );
             test=  ps.executeUpdate();
           // System.out.println("<<<<<< Updated am_group_asset_main_archive >>>>>>>>" + test );

   String upd_Am_invoice_no_qry=
           "update am_invoice_no set lpo='"+lpo+"',invoice_no='"+invoiceNum+"' WHERE group_id ='"+gid+"'";
   
   String upd_am_group_asset_qry=
           "update am_group_asset set lpo='"+lpo+"',invoice_no='"+invoiceNum+"'  WHERE group_id ='"+gid+"'";

    ad.updateAssetStatusChange(upd_Am_invoice_no_qry);
    ad.updateAssetStatusChange(upd_am_group_asset_qry);
    
            if(this.costPrice_flag.equalsIgnoreCase("N"))
            {
            updateCreatedGroupAsset();
            }

	    }
        catch (Exception ex)
        {
	           System.out.println("WARNING: update am_group_asset_main in GroupAssetBean: "+ex.getMessage());
	    }
        
        
		return Long.parseLong(gid);
	}



	//ADD RAISE ENTRY
	private void  updateCreatedGroupAsset()
	{
		String update_created_asset_qry ="update am_group_asset set process_flag= ?, " +
	    " DATE_PURCHASED=?, DEP_RATE=?, "+
	    " SUPPLIER_NAME=?," +
	    " ACCUM_DEP=?," +
	    " COST_PRICE=?, DEP_END_DATE=?, RESIDUAL_VALUE=?," +
	    " POSTING_DATE=?, EFFECTIVE_DATE=?, "+
	    " VATABLE_COST=?,VAT=?, WH_TAX=?, WH_TAX_AMOUNT=?,SUBJECT_TO_VAT=?, " +
	    " RAISE_ENTRY=?, DEP_YTD=?," +
	    " [USER_ID]=?," +
	    " PROVINCE=?, WAR_START_DATE=?, WAR_MONTH=?, " +
	    " WAR_EXPIRY_DATE=?," +
	    " PART_PAY=?,FULLY_PAID=?,DEFER_PAY=?, "+
	    " [AMOUNT_PTD] =?,[AMOUNT_REM] = ?" +
	    " TRANPORT_COST = ?,OTHER_COST = ?" +
	    "  where group_id = ? " ;

		Codes code = new Codes();
		int itemsCount = Integer.parseInt(no_of_items);
		

			try
			{
				Connection con = dbConnection.getConnection("legendPlus");
	             PreparedStatement ps = con.prepareStatement(update_created_asset_qry);
				ps.setString(1, "N");
	            ps.setString(2, DateManipulations.CalendarToDb(date_of_purchase));
	            ps.setString(3, getDepreciationRate(category_id));
	            ps.setInt(4, Integer.parseInt(supplied_by));
	            ps.setInt(5, 0);
	            ps.setDouble(6,  Double.parseDouble(cost_price) / itemsCount);
	            ps.setDate(7, dbConnection.dateConvert(depreciation_end_date));
	            ps.setDouble(8, Double.parseDouble(residual_value));
	            ps.setDate(9, dbConnection.dateConvert(new java.util.Date()));
	            ps.setDate(10, dbConnection.dateConvert(depreciation_start_date));
	            ps.setDouble(11, Double.parseDouble(vatable_cost)/ itemsCount);
	            ps.setDouble(12, Double.parseDouble(vat_amount)/ itemsCount);
	            ps.setString(13, wh_tax_cb);
	            ps.setDouble(14, Double.parseDouble(wh_tax_amount)/ itemsCount);
	            ps.setString(15, subject_to_vat);
	            ps.setString(16, "N");
	            ps.setString(17, "0");
	            ps.setString(18, user_id);
	            ps.setString(19, province);
	            ps.setDate(20, dbConnection.dateConvert(warrantyStartDate));
	            ps.setInt(21, Integer.parseInt(noOfMonths));
	            ps.setDate(22, dbConnection.dateConvert(expiryDate));
	            ps.setString(23, partPAY);
	            ps.setString(24, fullyPAID);
	            ps.setString(25, deferPay);
	            ps.setDouble(26, Double.parseDouble(amountPTD)/ itemsCount);
	            ps.setDouble(27, (Double.parseDouble(cost_price)/ itemsCount)-(Double.parseDouble(amountPTD)/ itemsCount));
	            ps.setDouble(28, Double.parseDouble(transport_cost)/ itemsCount);
	            ps.setDouble(29, Double.parseDouble(other_cost)/ itemsCount);
	            ps.setString(30, group_id);
	            int result = ps.executeUpdate();
	            System.out.println("<<<<<<<<<<<<<<<<<<< Updated am_group_asset >>>>>>>>>>>>" + result);
			}
			catch (Exception ex)
			{
				System.out.println("WARNING: update am_group_asset in GroupAssetRepost: " + ex.getMessage());
			}
			
	}

	public boolean deleteGrpImage(String query)
    {
		boolean result = false;//pessimistic
    	
        try
        {
        	Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(query);
        	int val =ps.executeUpdate();
        	System.out.println("Result After Deleting From Group Image : " + val);
        	if(val > 0)
        	{
        		result=true;
        	}
         }
        catch (Exception ex)
        {
            System.out.println("GroupAssetBean: deleteGrpImage -" + ex);
        }
       
        
		return result;
	}

	public String checkGroupAssetLedgerAccount(String category,String branch)
	{
		String assetledgeraccount="";
		System.out.println("category "+category);
		System.out.println("branch "+branch);
		String query=" select c.iso_code,"
					+" (select accronym from am_ad_ledger_type where series = substring(b.group_asset_account,1,1)),"
					+" b.default_branch,"
					+" b.group_asset_account,"
					+" c.iso_code +(select accronym from am_ad_ledger_type where series = substring(b.group_asset_account,1,1))+"
					+" b.default_branch + b.group_asset_account asd"
					+" from am_ad_category a,am_ad_branch d, "
					+" AM_GB_CURRENCY_CODE c, am_gb_company b"
					+" where a.currency_id = c.currency_id"
					+" and a.category_code = '"+category+"'"
					+" and d.branch_code = '"+branch+"'";
		System.out.println("query in checkAssetLedgerAccount >>>> " + query);

		

		try {

			Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			if (rs.next())
			 {

				 assetledgeraccount  = rs.getString("asd");


			 }
		   }
			catch (Exception er)
			{
			 er.printStackTrace();

			}
			
		
	return 	assetledgeraccount;
	}

	public String vendorName (String groupID)
	{
		String vendor="";
		String query="select  vendor_name from am_ad_vendor where vendor_id=" +
                "(select supplier_name from am_group_asset_main where group_id='"+groupID+"')";

		

		try {

			Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			if (rs.next())
			 {

				vendor  = rs.getString("vendor_name");


			 }
		   }
			catch (Exception er)
			{
			 er.printStackTrace();

			}
			
	return 	vendor;
	}

        public void insGrpApprovalRemark(String id,String next_sprv,String Remark,String status,
                int apprvCount,String systemIP,String tranID,String tranType)
        {
          

            String insGrpRemarkQry="insert into am_approval_remark (asset_id,supervisorID,Remark,Status," +
                    "ApprovalLevel,System_IP,Transaction_id,tran_type) values (?,?,?,?,?,?,?,?)";
            try
            {
            	Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(insGrpRemarkQry);
                ps.setString(1, id);
                ps.setInt(2, Integer.parseInt(next_sprv));
                ps.setString(3, Remark);
                ps.setString(4, status);
                ps.setInt(5, apprvCount);
                ps.setString(6, systemIP);
                ps.setInt(7, Integer.parseInt(tranID));
                ps.setString(8, tranType);
                boolean result = ps.execute();
            }
            catch(Exception ex)
            {
                System.out.println("WARN:Error inserting into am_approval_remark ->" + ex);
            }
            
        }

            public String setGroupPendingTrans(String[] a, String code,int assetCode){

        int transaction_level=0;
        String transaction_type = "";
        
 String pq =
	 "insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description," +
	 "effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time," +
	 "transaction_id,batch_id,transaction_level,asset_code) " +
	 "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
 String tranLevelQuery = "select level,Transaction_type from approval_level_setup where code ='"+code+"'";
       
        try
        {
        	Connection con = dbConnection.getConnection("legendPlus");
            PreparedStatement ps = con.prepareStatement(tranLevelQuery);
            ResultSet  rs = ps.executeQuery();
            while(rs.next()){
            transaction_level = rs.getInt(1);
            transaction_type = rs.getString(2);
            }
            ps = con.prepareStatement(pq);

            SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

            mtid =  new ApplicationHelper().getGeneratedId("am_asset_approval");

            ps.setString(1, (a[0]==null)?"":a[0]);
            ps.setString(2, (a[1]==null)?"":a[1]);
            ps.setString(3, (a[2]==null)?"":a[2]);
            ps.setDouble(4, (a[3]==null)?0:Double.parseDouble(a[3]));
            //System.out.println("Posting_Date b4 conversion in setPendingTrans : " +  a[4]);
            ps.setDate(5, (a[4])==null?null:dbConnection.dateConvert(a[4]));
            //System.out.println("Posting_Date after setPendingTrans : " +  a[4]);
            ps.setString(6, (a[5]==null)?"":a[5]);
           // System.out.println("effective_date b4 conversion in setPendingTrans : " +  a[6]);
            ps.setDate(7,(a[6])==null?null:dbConnection.dateConvert(a[6]));
            //System.out.println("effective_date after conversion in setPendingTrans : " +  a[6]);
            ps.setString(8, (a[7]==null)?"":a[7]);
            ps.setString(9, (a[8]==null)?"":a[8]); //asset_status
            ps.setString(10, transaction_type); //Transaction Type
//            ps.setString(10, (a[9]==null)?"":a[9]);
            ps.setString(11, a[10]);
            ps.setString(12, timer.format(new java.util.Date()));
            ps.setString(13,mtid);
            ps.setString(14, mtid);
            ps.setInt(15, transaction_level);
            ps.setInt(16, assetCode);
            ps.execute();

        }
        catch(Exception er)
        {
            System.out.println(">>>GroupAssetBean:setGroupPendingTrans(>>>>>>" + er);

        }
        
        return mtid;
    }
            

        	public long [] insertGroupAssetTwoRecord() throws Exception, Throwable {
        		//System.out.println("INSIDE INSERT GROUP ASSET RECORD OF GROUP ASSET BEAN");
        		String[] budget = getBudgetInfo();
        		double[] bugdetvalues = getBudgetValues();
        		int DONE = 0; // everything is oK
        		int BUDGETENFORCED = 1; // EF budget = Yes, CF = NO, ERROR_FLAG
        		int BUDGETENFORCEDCF = 2; // EF budget = Yes, CF = Yes, ERROR_FLAG
        		int ASSETPURCHASEBD = 3; // asset falls into no quarter purchase date

        		long [] value = new long[2];

        		// older than bugdet
        		String Q = getQuarter();
        		//System.out.println("BUDGET VALUE  =======  "  + budget[3]);
        		if(budget[3].equalsIgnoreCase("N")){
        			value[1]=createGroupAsset2();
        			value[0]= DONE;
        		}
        		else if(budget[3].equalsIgnoreCase("Y")){
        		if (!Q.equalsIgnoreCase("NQ")) {
        			if (budget[3].equalsIgnoreCase("Y")
        					&& budget[4].equalsIgnoreCase("N")) {
        				if (chkBudgetAllocation(Q, bugdetvalues, false)) {
        					updateBudget(Q, budget);
        					value[1]=createGroupAsset2();
        					value[0]= DONE;
        				} else {
        					value[0]=  BUDGETENFORCED;
        				}

        			} else if (budget[3].equalsIgnoreCase("Y")
        					&& budget[4].equalsIgnoreCase("Y")) {
        				if (chkBudgetAllocation(Q, bugdetvalues, true)) {
        					updateBudget(Q, budget);
        					value[1]=createGroupAsset2();
        					value[0]=  DONE;
        				} else {
        					value[0]=  BUDGETENFORCEDCF;
        				}

        			} else {
        				value[1]=createGroupAsset2();
        				value[0]= DONE;
        			}
        		} else {
        			//value[1]=createGroup();
        			value[0]= ASSETPURCHASEBD;
        		}
        	}
        	else{}
        		return value;

        	}

        	/**
        	 * createGroupAsset2
        	 *
        	 * @return boolean
        	 */


        	public long createGroupAsset2() throws Exception,Throwable
        	{
            	
            	
        		aprecords = new ApprovalRecords();
        		//System.out.println(">>>>>>>>> INSIDE CREATE GROUP IN GROUP ASSET BEAN <<<<<<<<<< ");
        		StringBuffer b = new StringBuffer(400);
                        StringBuffer sb = new StringBuffer(400);
        		Codes code = new Codes();
        		if (no_of_items == null || no_of_items.equals("")) {
        			no_of_items = "0";
        		}
        		if (province == null || province.equals("")) {
        			province = "0";
        		}
        		int itemsCount = Integer.parseInt(no_of_items);
        		if (noOfMonths == null || noOfMonths.equals("")) {
        			noOfMonths = "0";
        		}
        		if (warrantyStartDate == null || warrantyStartDate.equals("")) {
        			warrantyStartDate = null;
        		}
        		if (expiryDate == null || expiryDate.equals("")) {
        			expiryDate = null;
        		}
        		if(supervisor.equals("") ||supervisor=="0")
        		{
        			supervisor= user_id;
        		}
        		if (make == null || make.equals(""))
        		{
        			make="0";
        		} 
        	//	System.out.println(">>>>>>>>>  category_id <<<<<<<<<< " +category_id+"    sub_category_id: "+sub_category_id);
        		String subcategoryId = sub_category_id;
        		//System.out.println(">>>>>>>>> BEFORE CALLING CREATE GROUP MAIN IN GROUP ASSET BEAN <<<<<<<<<< ");
        		long gid = createAsset2GroupMain();
        //		ad.setPendingTrans(ad.setApprovalDataGroupAsset2(gid),"47"); 
        		for (int x = 0; x < itemsCount; x++)
        		{  
        			try {

        			//asset_id = new legend.AutoIDSetup().getIdentity(branch_id,department_id, section_id, category_id);
        			asset_id = new ApplicationHelper().getGeneratedId("am_group_asset2");
                              
                     asset_id = asset_id+"-Two";
         
//        		System.out.println(">>>>>>>>>  category_id <<<<<<<<<< " + category_id);

        		String query = "INSERT INTO AM_GROUP_ASSET2( ASSET_ID,"
        			+ "REGISTRATION_NO,BRANCH_ID,DEPT_ID,"
        			+ "CATEGORY_ID,SECTION_ID,DESCRIPTION,"
        			+ "VENDOR_AC,DATE_PURCHASED,DEP_RATE,"
        			+ "ASSET_MAKE,ASSET_MODEL,ASSET_SERIAL_NO,"
        			+ "ASSET_ENGINE_NO,SUPPLIER_NAME,"
        			+ "ASSET_USER,ASSET_MAINTENANCE,"
        			+ "COST_PRICE,DEP_END_DATE,RESIDUAL_VALUE,"
        			+ "AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,"
        			+ "POSTING_DATE,EFFECTIVE_DATE,"
        			+ "PURCHASE_REASON,LOCATION,VATABLE_COST,"
        			+ "VAT,REQ_DEPRECIATION,SUBJECT_TO_VAT,"
        			+ "WHO_TO_REM,	EMAIL1,	WHO_TO_REM_2,"
        			+ "EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,[USER_ID], PROVINCE,"
        			+ " WAR_START_DATE, WAR_MONTH, WAR_EXPIRY_DATE,BRANCH_CODE,DEPT_CODE,"
        			+ "SECTION_CODE,CATEGORY_CODE,GROUP_ID,AMOUNT_PTD,AMOUNT_REM,ACCUM_DEP," +
                                "PART_PAY,FULLY_PAID,Asset_Status,"
        			+ "supervisor,LPO,BAR_CODE,req_redistribution,Raise_entry,defer_pay,process_flag ," +
                                "SBU_CODE,post_flag,Invoice_no,workstationIp, SPARE_3, SPARE_4, SPARE_5, SPARE_6,sub_category_id,SUB_CATEGORY_CODE"
        			+ " )	VALUES " +
        					"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
        					"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        		
        		Connection con = dbConnection.getConnection("legendPlus");
                PreparedStatement ps = con.prepareStatement(query);
        		amountPTD = amountPTD.replaceAll(",","");
        		ps.setString(1, asset_id);
        		ps.setString(2, registration_no);
        		ps.setString(3, branch_id);
        		ps.setString(4, department_id);
        		ps.setString(5, category_id);
        		ps.setString(6, section_id);
        		ps.setString(7, description.toUpperCase());
        		ps.setString(8, vendor_account);
        		ps.setString(9, DateManipulations.CalendarToDb(date_of_purchase));
                String TechQuery = "select dep_rate from am_ad_category where category_id = category_id";
                String rate = aprecords.getCodeName(TechQuery); 
        		ps.setString(10, rate);
        		ps.setString(11, make);
        		ps.setString(12, model);
        		ps.setString(13, serial_number);
        		ps.setString(14, engine_number);
        		ps.setInt(15, Integer.parseInt(supplied_by));
        		ps.setString(16, user);
        		ps.setInt(17, Integer.parseInt(maintained_by));
        		ps.setDouble(18, Double.parseDouble(cost_price) / itemsCount);
        		ps.setDate(19, dbConnection.dateConvert(depreciation_end_date));
        		ps.setDouble(20, Double.parseDouble(residual_value));
        		ps.setString(21, authorized_by);
        		ps.setString(22, wh_tax_cb);
        		ps.setDouble(23, Double.parseDouble(wh_tax_amount) / itemsCount);
        		ps.setString(24,DateManipulations.CalendarToDb(posting_date));
        		ps.setDate(25, dbConnection.dateConvert(depreciation_start_date));
        		ps.setString(26, reason);
        		ps.setInt(27, Integer.parseInt(location));
        		ps.setDouble(28, Double.parseDouble(vatable_cost) / itemsCount);
        		ps.setDouble(29, Double.parseDouble(vat_amount) / itemsCount);
        		ps.setString(30, require_depreciation);
        		ps.setString(31, subject_to_vat);
        		ps.setString(32, who_to_rem);
        		ps.setString(33, email_1);
        		ps.setString(34, who_to_rem_2);
        		ps.setString(35, email2);
        		ps.setInt(36, Integer.parseInt(state));
        		ps.setInt(37, Integer.parseInt(driver));
        		ps.setString(38, spare_1);
        		ps.setString(39, spare_2);
        		ps.setString(40, user_id);
        		ps.setInt(41, Integer.parseInt(province));
        		ps.setDate(42, dbConnection.dateConvert(warrantyStartDate));
        		ps.setInt(43, Integer.parseInt(noOfMonths));
        		ps.setDate(44, dbConnection.dateConvert(expiryDate));
        		ps.setString(45,code.getBranchCode(branch_id));
        		ps.setString(46,code.getDeptCode(department_id));
        		ps.setString(47,code.getSectionCode(section_id));
        		ps.setString(48,code.getCategoryCode(category_id));
        		ps.setLong(49, gid);
        		ps.setDouble(50, Double.parseDouble(amountPTD) / itemsCount);
        		ps.setDouble(51, ((Double.parseDouble(cost_price) / itemsCount)-(Double.parseDouble(amountPTD)/ itemsCount)));
        		ps.setDouble(52, accum_dep);
        		ps.setString(53,"N");
        		ps.setString(54,"Y");
                ps.setString(55,status);
                ps.setString(56,supervisor);
                ps.setString(57,lpo);
                ps.setString(58,bar_code);
                ps.setString(59,require_redistribution);
                ps.setString(60,"N");
                ps.setString(61,"N");
                ps.setString(62,"N");
                ps.setString(63, sbu_code);
                ps.setString(64, "P");
                ps.setString(65, invoiceNum);
                ps.setString(66, workstationIp); 
        		ps.setString(67, spare_3);
        		ps.setString(68, spare_4);
        		ps.setString(69, spare_5);
        		ps.setString(70, spare_6);
//        		System.out.println(">>>>>>subcategoryId: "+subcategoryId+"    sub_category_id: "+sub_category_id);
        		ps.setString(71, subcategoryId);
        		ps.setString(72,code.getSubCategoryCode(subcategoryId));
        		
                boolean result = ps.execute();
                archiveAsset2Update(asset_id,itemsCount,gid);
        			}

                                catch (Exception r) {
        				System.out.println("INFO: Error creating group asset2 >>" + r);
        			} 
        			
                }
        		//ad.updateGroupAssetStatus(Long.toString(gid));
        		return gid;
        	}


        	public long createAsset2GroupMain() throws Exception {
            	//dbConnection = new MagmaDBConnection();
            	
        		//System.out.println(">>>>>> INSIDE CREATE GROUP MAIN OF GROUP ASSET BEAN <<<<<<");
        		StringBuffer b = new StringBuffer(400);
                        StringBuffer sbf = new StringBuffer(400);
        		Codes code = new Codes();
        		if (no_of_items == null || no_of_items.equals("")) {
        			no_of_items = "0";
        		}
        		if (province == null || province.equals("")) {
        			province = "0";
        		}
        		int itemsCount = Integer.parseInt(no_of_items);
        		if (noOfMonths == null || noOfMonths.equals("")) {
        			noOfMonths = "0";
        		}
        		if (warrantyStartDate == null || warrantyStartDate.equals("")) {
        			warrantyStartDate = null;
        		}
        		if (expiryDate == null || expiryDate.equals("")) {
        			expiryDate = null;
        		}
        		if(supervisor.equals("")||supervisor=="0")
        		{
        			supervisor= user_id;
        		}
        		if (make == null || make.equals(""))
        		{
        			make="0";
        		}
        		long gid=0;
//        		System.out.println("Before Generating gid >>>>>>>>>> "+group_id);
        		String groupid =  new ApplicationHelper().getGeneratedId("AM_GROUP_ASSET_MAIN"); 
//        		System.out.println("After Generating gid >>>>>>>>>> "+groupid);
        		 gid = Long.parseLong(groupid);
        		String query = "SET IDENTITY_INSERT AM_GROUP_ASSET2_MAIN ON  INSERT INTO AM_GROUP_ASSET2_MAIN(GROUP_ID,QUANTITY,"
        			+ "REGISTRATION_NO,BRANCH_ID,DEPT_ID,"
        			+ "CATEGORY_ID,SECTION_ID,DESCRIPTION,"
        			+ "VENDOR_AC,DATE_PURCHASED,DEP_RATE,ACCUM_DEP,"
        			+ "ASSET_MAKE,ASSET_MODEL,ASSET_SERIAL_NO,"
        			+ "ASSET_ENGINE_NO,SUPPLIER_NAME,"
        			+ "ASSET_USER,ASSET_MAINTENANCE,"
        			+ "COST_PRICE,DEP_END_DATE,RESIDUAL_VALUE,"
        			+ "AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,"
        			+ "POSTING_DATE,EFFECTIVE_DATE,"
        			+ "PURCHASE_REASON,LOCATION,VATABLE_COST,"
        			+ "VAT,REQ_DEPRECIATION,SUBJECT_TO_VAT,"
        			+ "WHO_TO_REM,	EMAIL1,	WHO_TO_REM_2,"
        			+ "EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,[USER_ID], PROVINCE,"
        			+ " WAR_START_DATE, WAR_MONTH, WAR_EXPIRY_DATE,BRANCH_CODE,DEPT_CODE,"
        			+ "SECTION_CODE,CATEGORY_CODE,raise_entry,AMOUNT_PTD,AMOUNT_REM,"
        			+ "PART_PAY,FULLY_PAID,Asset_Status,supervisor,"
        			+ "LPO,BAR_CODE,req_redistribution,defer_pay ,Vatable_Cost_Bal,process_flag," +
                                "sbu_code,pend_GrpAssets,Invoice_No,workstationIp, SPARE_3, SPARE_4, SPARE_5, SPARE_6,sub_category_id,SUB_CATEGORY_CODE"
        			+ ")	VALUES(";
        		b.append(query);
        		b.append(gid);
        		b.append(",");				
        		b.append(itemsCount);
        		b.append(",'");
        		b.append(registration_no);
        		b.append("',");
        		b.append(branch_id);
        		b.append(",");
        		b.append(department_id);
        		b.append(",");
        		b.append(category_id);
        		b.append(",");
        		b.append(section_id);
        		b.append(",'");
        		b.append(description.toUpperCase());
        		b.append("','");
        		b.append(vendor_account);
        		b.append("','");
        		b.append(DateManipulations.CalendarToDb(date_of_purchase));
        		b.append("',");
        		b.append(depreciation_rate);
        		b.append(",");
        		b.append(accum_dep);
        		b.append(",");
        		b.append(make);
        		b.append(",'");
        		b.append(model);
        		b.append("','");
        		b.append(serial_number);
        		b.append("','");
        		b.append(engine_number);
        		b.append("',");
        		b.append(supplied_by);
        		b.append(",'");
        		b.append(user);
        		b.append("',");
        		b.append(maintained_by);
        		b.append(",");
        		b.append(Double.parseDouble(cost_price) );
        		b.append(",'");
        		b.append(dbConnection.dateConvert(depreciation_end_date));
        		b.append("',");
        		b.append(residual_value);
        		b.append(",'");
        		b.append(authorized_by);
        		b.append("','");
        		b.append(wh_tax_cb);
        		b.append("',");
        		b.append(Double.parseDouble(wh_tax_amount));
        		b.append(",'");
        		b.append(DateManipulations.CalendarToDb(posting_date));
        		b.append("','");
        		b.append(dbConnection.dateConvert(depreciation_start_date));
        		b.append("','");
        		b.append(reason);
        		b.append("',");
        		b.append(location);
        		b.append(",");
        		b.append(Double.parseDouble(vatable_cost));
        		b.append(",");
        		b.append(Double.parseDouble(vat_amount));
        		b.append(",'");
        		b.append(require_depreciation);
        		b.append("','");
        		b.append(subject_to_vat);
        		b.append("','");
        		b.append(who_to_rem);
        		b.append("','");
        		b.append(email_1);
        		b.append("','");
        		b.append(who_to_rem_2);
        		b.append("','");
        		b.append(email2);
        		b.append("',");
        		b.append(state);
        		b.append(",");
        		b.append(driver);
        		b.append(",'");
        		b.append(spare_1);
        		b.append("','");
        		b.append(spare_2);
        		b.append("',");
        		b.append(user_id + ",");
        		b.append(province);
        		b.append(",'");
        		b.append(dbConnection.dateConvert(warrantyStartDate));
        		b.append("',");
        		b.append(noOfMonths);
        		b.append(",'");
        		b.append(dbConnection.dateConvert(expiryDate));
        		b.append("','");
        		b.append(code.getBranchCode(branch_id));
        		b.append("','");
        		b.append(code.getDeptCode(department_id));
        		b.append("','");
        		b.append(code.getSectionCode(section_id));
        		b.append("','");
        		b.append(code.getCategoryCode(category_id));
        		b.append("','");
        		b.append("N");
        		b.append("',");
        		b.append(Double.parseDouble(amountPTD));
        		b.append(",");		
        		b.append((Double.parseDouble(cost_price))-(Double.parseDouble(amountPTD)));
        		b.append(",'");
        		b.append(partPAY);
        		b.append("','");
        		b.append(fullyPAID);
                b.append("','");
                b.append(status);
                b.append("','");
                b.append(supervisor);
                b.append("','");
                b.append(lpo);
                b.append("','");
                b.append(bar_code);
                b.append("','");
                b.append(require_redistribution);
                b.append("','");
                b.append(deferPay);
        		b.append("',");
        		b.append(Double.parseDouble(vatable_cost));
        		b.append(",'");
        		b.append("N");
        		b.append("','");
        		b.append(sbu_code);
        		b.append("',");
        		b.append(itemsCount);
                        b.append(",'");
        		b.append(invoiceNum);
                        b.append("','");
                        b.append(workstationIp);
                        b.append("','");
                        b.append(spare_3);
                		b.append("','");
                		b.append(spare_4);
                		b.append("','");
                		b.append(spare_5);
                		b.append("','");
                		b.append(spare_6);
                		b.append("',");
                		b.append(sub_category_id);
                		b.append(",'");
                		b.append(code.getSubCategoryCode(sub_category_id));                
        		b.append("') SET IDENTITY_INSERT AM_GROUP_ASSET2_MAIN OFF");
        			try
        			{
        				Connection con = dbConnection.getConnection("legendPlus");
        	             PreparedStatement ps = con.prepareStatement(b.toString());
        	             ps.executeUpdate();
        	            // getStatement().executeUpdate(b.toString());
                    String query_archive = "SET IDENTITY_INSERT AM_GROUP_ASSET2_MAIN_Archive ON INSERT INTO AM_GROUP_ASSET2_MAIN_Archive(group_id,QUANTITY,"
        			+ "REGISTRATION_NO,BRANCH_ID,DEPT_ID,"
        			+ "CATEGORY_ID,SECTION_ID,DESCRIPTION,"
        			+ "VENDOR_AC,DATE_PURCHASED,DEP_RATE,ACCUM_DEP,"
        			+ "ASSET_MAKE,ASSET_MODEL,ASSET_SERIAL_NO,"
        			+ "ASSET_ENGINE_NO,SUPPLIER_NAME,"
        			+ "ASSET_USER,ASSET_MAINTENANCE,"
        			+ "COST_PRICE,DEP_END_DATE,RESIDUAL_VALUE,"
        			+ "AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,"
        			+ "POSTING_DATE,EFFECTIVE_DATE,"
        			+ "PURCHASE_REASON,LOCATION,VATABLE_COST,"
        			+ "VAT,REQ_DEPRECIATION,SUBJECT_TO_VAT,"
        			+ "WHO_TO_REM,	EMAIL1,	WHO_TO_REM_2,"
        			+ "EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,[USER_ID], PROVINCE,"
        			+ " WAR_START_DATE, WAR_MONTH, WAR_EXPIRY_DATE,BRANCH_CODE,DEPT_CODE,"
        			+ "SECTION_CODE,CATEGORY_CODE,raise_entry,AMOUNT_PTD,AMOUNT_REM,"
        			+ "PART_PAY,FULLY_PAID,Asset_Status,supervisor,"
        			+ "LPO,BAR_CODE,req_redistribution,defer_pay ,Vatable_Cost_Bal,process_flag," +
                                "sbu_code,pend_GrpAssets,Invoice_No,workstationIp, SPARE_3, SPARE_4, SPARE_5, SPARE_6,sub_category_id,SUB_CATEGORY_CODE"
        			+ ")	VALUES(";
        		sbf.append(query_archive);
        		sbf.append(gid);
        		sbf.append(",");
        		sbf.append(itemsCount);
        		sbf.append(",'");
        		sbf.append(registration_no);
        		sbf.append("',");
        		sbf.append(branch_id);
        		sbf.append(",");
        		sbf.append(department_id);
        		sbf.append(",");
        		sbf.append(category_id);
        		sbf.append(",");
        		sbf.append(section_id);
        		sbf.append(",'");
        		sbf.append(description.toUpperCase());
        		sbf.append("','");
        		sbf.append(vendor_account);
        		sbf.append("','");
        		sbf.append(DateManipulations.CalendarToDb(date_of_purchase));
        		sbf.append("',");
        		sbf.append(depreciation_rate);
        		sbf.append(",");
        		sbf.append(accum_dep);
        		sbf.append(",");
        		sbf.append(make);
        		sbf.append(",'");
        		sbf.append(model);
        		sbf.append("','");
        		sbf.append(serial_number);
        		sbf.append("','");
        		sbf.append(engine_number);
        		sbf.append("',");
        		sbf.append(supplied_by);
        		sbf.append(",'");
        		sbf.append(user);
        		sbf.append("',");
        		sbf.append(maintained_by);
        		sbf.append(",");
        		sbf.append(Double.parseDouble(cost_price) );
        		sbf.append(",'");
        		sbf.append(dbConnection.dateConvert(depreciation_end_date));
        		sbf.append("',");
        		sbf.append(residual_value);
        		sbf.append(",'");
        		sbf.append(authorized_by);
        		sbf.append("','");
        		sbf.append(wh_tax_cb);
        		sbf.append("',");
        		sbf.append(Double.parseDouble(wh_tax_amount));
        		sbf.append(",'");
        		sbf.append(DateManipulations.CalendarToDb(posting_date));
        		sbf.append("','");
        		sbf.append(dbConnection.dateConvert(depreciation_start_date));
        		sbf.append("','");
        		sbf.append(reason);
        		sbf.append("',");
        		sbf.append(location);
        		sbf.append(",");
        		sbf.append(Double.parseDouble(vatable_cost));
        		sbf.append(",");
        		sbf.append(Double.parseDouble(vat_amount));
        		sbf.append(",'");
        		sbf.append(require_depreciation);
        		sbf.append("','");
        		sbf.append(subject_to_vat);
        		sbf.append("','");
        		sbf.append(who_to_rem);
        		sbf.append("','");
        		sbf.append(email_1);
        		sbf.append("','");
        		sbf.append(who_to_rem_2);
        		sbf.append("','");
        		sbf.append(email2);
        		sbf.append("',");
        		sbf.append(state);
        		sbf.append(",");
        		sbf.append(driver);
        		sbf.append(",'");
        		sbf.append(spare_1);
        		sbf.append("','");
        		sbf.append(spare_2);
        		sbf.append("',");
        		sbf.append(user_id + ",");
        		sbf.append(province);
        		sbf.append(",'");
        		sbf.append(dbConnection.dateConvert(warrantyStartDate));
        		sbf.append("',");
        		sbf.append(noOfMonths);
        		sbf.append(",'");
        		sbf.append(dbConnection.dateConvert(expiryDate));
        		sbf.append("','");
        		sbf.append(code.getBranchCode(branch_id));
        		sbf.append("','");
        		sbf.append(code.getDeptCode(department_id));
        		sbf.append("','");
        		sbf.append(code.getSectionCode(section_id));
        		sbf.append("','");
        		sbf.append(code.getCategoryCode(category_id));
        		sbf.append("','");
        		sbf.append("N");
        		sbf.append("',");
        		sbf.append(Double.parseDouble(amountPTD));
        		sbf.append(",");
        		sbf.append((Double.parseDouble(cost_price))-(Double.parseDouble(amountPTD)));
        		sbf.append(",'");
        		sbf.append(partPAY);
        		sbf.append("','");
        		sbf.append(fullyPAID);
                sbf.append("','");
                sbf.append(status);
                sbf.append("','");
                sbf.append(supervisor);
                sbf.append("','");
                sbf.append(lpo);
                sbf.append("','");
                sbf.append(bar_code);
                sbf.append("','");
                sbf.append(require_redistribution);
                sbf.append("','");
                sbf.append(deferPay);
        		sbf.append("',");
        		sbf.append(Double.parseDouble(vatable_cost));
        		sbf.append(",'");
        		sbf.append("N");
        		sbf.append("','");
        		sbf.append(sbu_code);
        		sbf.append("',");
        		sbf.append(itemsCount);
                sbf.append(",'");
        		sbf.append(invoiceNum);
                sbf.append("','");
                sbf.append(workstationIp);
                sbf.append("','");
                sbf.append(spare_3);
                sbf.append("','");
                sbf.append(spare_4);
                sbf.append("','");
                sbf.append(spare_5);
                sbf.append("','");
                sbf.append(spare_6);
                sbf.append("',");
                sbf.append(sub_category_id);
                sbf.append(",'");
                sbf.append(code.getSubCategoryCode(sub_category_id));                 
        		sbf.append("')  SET IDENTITY_INSERT AM_GROUP_ASSET2_MAIN_Archive OFF ");
//                        System.out.println("Query Used>>>>>>>>>> " + sbf.toString());
        		Connection con2 = dbConnection.getConnection("legendPlus");
	             PreparedStatement ps2 = con2.prepareStatement(sbf.toString());
	             ps.executeUpdate();
                       // getStatement().executeUpdate(sbf.toString());
        			}
        			catch (Exception r)
        			{
        				System.out.println("INFO: Error creating AM_GROUP_ASSET2_MAIN_Archive >>" + r);
        			}
        			

        		return gid;
        	}

            public long getGroupAsset2ID(String aid) {

               
                long numRecords=0;
                //boolean multipleComponent = false;
                String query = "SELECT distinct group_id FROM am_group_asset2 where asset_id='"+aid +"'";
                /*System.out.println("<<<<<<<< Inside getGrtoupID >>>>>>>>");
                System.out.println("Query >>>> " + query);*/
                try {
                	Connection con = dbConnection.getConnection("legendPlus");
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next())
                    {
                        numRecords = rs.getLong(1);
                       // System.out.println("Group_ID >>>> " + numRecords);
                    }

                } catch (Exception ex) {
                    System.out.println("GroupAssetBean: getGroupAsset2ID(): WARN: Error determining  group id->" +
                                       ex);
                } 

                return numRecords;
            }

        	public void getCreatedAsset2Group(long id) {
            	dbConnection = new MagmaDBConnection();
            	
        		String selectQuery = "SELECT group_id, quantity, Registration_No, " +
        				"Branch_ID,province, Dept_ID, Category_ID,Sub_Category_ID, section_id, Description, " +
        				"Vendor_AC, Date_purchased, dep_rate, asset_make, asset_model, " +
        				"asset_serial_no, asset_engine_no, supplier_name, asset_user, " +
        				"asset_maintenance, Cost_Price, dep_end_date, residual_value, " +
        				"authorized_by, wh_tax, wh_tax_amount, req_redistribution, " +
        				"Posting_Date, effective_date, purchase_reason, location, " +
        				"Vatable_Cost, Vat, Req_Depreciation, Subject_TO_Vat, " +
        				"Who_TO_Rem, email1, who_to_rem_2, email2, Raise_entry, " +
        				"dep_ytd, Section, Asset_Status, state, driver, spare_1, " +
        				"spare_2, spare_3,spare_4,spare_5,spare_6, user_ID,WAR_START_DATE, WAR_MONTH, " +
        				"WAR_EXPIRY_DATE, branch_code, dept_code, section_code, " +
        				"category_code,AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID,ACCUM_DEP," +
                                        "LPO,BAR_CODE,defer_pay,SBU_CODE,Invoice_no  " +
        				" FROM am_group_asset2_main WHERE GROUP_ID="+id;
        		try {
        			Connection con = dbConnection.getConnection("legendPlus");
                    PreparedStatement ps = con.prepareStatement(selectQuery);
                    ResultSet rs = ps.executeQuery();
        			//rs = getStatement().executeQuery(selectQuery);
        			while(rs.next())
        				{
        				group_id =rs.getString("GROUP_ID");
        				no_of_items=rs.getString("quantity");
        				registration_no = rs.getString("REGISTRATION_NO");
                        branch_id = rs.getString("BRANCH_ID");
                        province = rs.getString("PROVINCE");
                        department_id = rs.getString("DEPT_ID");
                        category_id = rs.getString("CATEGORY_ID");
                        section_id = rs.getString("SECTION_ID");
                        description = rs.getString("DESCRIPTION");
                        vendor_account = rs.getString("VENDOR_AC");
                        depreciation_rate = rs.getString("DEP_RATE");
                        make = rs.getString("ASSET_MAKE");
                        model = rs.getString("ASSET_MODEL");
                        serial_number = rs.getString("ASSET_SERIAL_NO");
                        engine_number = rs.getString("ASSET_ENGINE_NO");
                        supplied_by = rs.getString("SUPPLIER_NAME");
                        authuser = rs.getString("ASSET_USER");
                        maintained_by = rs.getString("ASSET_MAINTENANCE");
                        cost_price = rs.getString("COST_PRICE");
                        depreciation_end_date = dbConnection.formatDate(rs.getDate("DEP_END_DATE"));
                        residual_value = rs.getString("RESIDUAL_VALUE");
                        authorized_by = rs.getString("AUTHORIZED_BY");
                        wh_tax_cb = rs.getString("WH_TAX");
                        wh_tax_amount = rs.getString("WH_TAX_AMOUNT");
                        require_redistribution = rs.getString("REQ_REDISTRIBUTION");
                        posting_date =DateManipulations.DateToCalendar(sdf.format(rs.getDate("POSTING_DATE")));
                        depreciation_start_date = dbConnection.formatDate(rs.getDate("EFFECTIVE_DATE"));
                        reason = rs.getString("PURCHASE_REASON");
                        location = rs.getString("LOCATION");
                        vatable_cost = rs.getString("VATABLE_COST");
                        vat_amount = rs.getString("VAT");
                        require_depreciation = rs.getString("REQ_DEPRECIATION");
                        subject_to_vat = rs.getString("SUBJECT_TO_VAT");
                        who_to_rem = rs.getString("WHO_TO_REM");
                        email_1 = rs.getString("EMAIL1");
                        who_to_rem_2 = rs.getString("WHO_TO_REM_2");
                        email2 = rs.getString("EMAIL2");
                        raise_entry = rs.getString("RAISE_ENTRY");
                        status = rs.getString("ASSET_STATUS");
                        state = rs.getString("STATE");
                        driver = rs.getString("DRIVER");
                        spare_1 = rs.getString("SPARE_1");
                        spare_2 = rs.getString("SPARE_2");
                        spare_3 = rs.getString("SPARE_3");
                        spare_4 = rs.getString("SPARE_4");
                        spare_5 = rs.getString("SPARE_5");
                        spare_6 = rs.getString("SPARE_6");
                        user = rs.getString("USER_ID");
                        warrantyStartDate = dbConnection.formatDate(rs.getDate(
                        "WAR_START_DATE"));
                        noOfMonths = rs.getString("WAR_MONTH");
                        expiryDate =  dbConnection.formatDate(rs.getDate("WAR_EXPIRY_DATE"));
                        amountPTD = rs.getString("AMOUNT_PTD");
                        amountREM = rs.getString("AMOUNT_REM");
                        partPAY = rs.getString("PART_PAY");
                        fullyPAID = rs.getString("FULLY_PAID");
                        accum_dep = rs.getDouble("ACCUM_DEP");
                        date_of_purchase =DateManipulations.DateToCalendar(sdf.format(rs.getDate("DATE_PURCHASED")));
                        lpo=rs.getString("LPO");
                        bar_code=rs.getString("BAR_CODE");
                        deferPay= rs.getString("defer_pay");
                        sbu_code= rs.getString("SBU_CODE");
        		invoiceNum= rs.getString("Invoice_no");
                                }
        		} catch (Exception er) {
        			System.out.println("INFO: Error fetching groupid in getCreatedAsset2Group >>" + er);
        			//id = "";
        		} 

        		//return id;
        	}


            public void archiveAsset2Update(String asset_id,int itemsCount,long gid)
            {
            	dbConnection = new MagmaDBConnection();
            	
                Codes code = new Codes();
                String query = "INSERT INTO AM_GROUP_ASSET2_ARCHIVE( ASSET_ID,"
    			+ "REGISTRATION_NO,BRANCH_ID,DEPT_ID,"
    			+ "CATEGORY_ID,SECTION_ID,DESCRIPTION,"
    			+ "VENDOR_AC,DATE_PURCHASED,DEP_RATE,"
    			+ "ASSET_MAKE,ASSET_MODEL,ASSET_SERIAL_NO,"
    			+ "ASSET_ENGINE_NO,SUPPLIER_NAME,"
    			+ "ASSET_USER,ASSET_MAINTENANCE,"
    			+ "COST_PRICE,DEP_END_DATE,RESIDUAL_VALUE,"
    			+ "AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,"
    			+ "POSTING_DATE,EFFECTIVE_DATE,"
    			+ "PURCHASE_REASON,LOCATION,VATABLE_COST,"
    			+ "VAT,REQ_DEPRECIATION,SUBJECT_TO_VAT,"
    			+ "WHO_TO_REM,	EMAIL1,	WHO_TO_REM_2,"
    			+ "EMAIL2,	STATE,DRIVER,SPARE_1,SPARE_2,[USER_ID], PROVINCE,"
    			+ " WAR_START_DATE, WAR_MONTH, WAR_EXPIRY_DATE,BRANCH_CODE,DEPT_CODE,"
    			+ "SECTION_CODE,CATEGORY_CODE,GROUP_ID,AMOUNT_PTD,AMOUNT_REM,ACCUM_DEP," +
                            "PART_PAY,FULLY_PAID,Asset_Status,"
    			+ "supervisor,LPO,BAR_CODE,req_redistribution,Raise_entry,defer_pay,process_flag ," +
                            "SBU_CODE,post_flag,Invoice_no,workstationIp, SPARE_3, SPARE_4, SPARE_5, SPARE_6,sub_category_id,SUB_CATEGORY_CODE"
    			+ " )	VALUES " +
    					"(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
    					"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                try {
                	Connection con = dbConnection.getConnection("legendPlus");
                    PreparedStatement ps = con.prepareStatement(query);
    		amountPTD = amountPTD.replaceAll(",","");
    		ps.setString(1, asset_id);
    		ps.setString(2, registration_no);
    		ps.setString(3, branch_id);
    		ps.setString(4, department_id);
    		ps.setString(5, category_id);
    		ps.setString(6, section_id);
    		ps.setString(7, description.toUpperCase());
    		ps.setString(8, vendor_account);
    		ps.setString(9, DateManipulations.CalendarToDb(date_of_purchase));
    		ps.setString(10, depreciation_rate);
    		ps.setString(11, make);
    		ps.setString(12, model);
    		ps.setString(13, serial_number);
    		ps.setString(14, engine_number);
    		ps.setInt(15, Integer.parseInt(supplied_by));
    		ps.setString(16, user);
    		ps.setInt(17, Integer.parseInt(maintained_by));
    		ps.setDouble(18, Double.parseDouble(cost_price) / itemsCount);
    		ps.setDate(19, dbConnection.dateConvert(depreciation_end_date));
    		ps.setDouble(20, Double.parseDouble(residual_value));
    		ps.setString(21, authorized_by);
    		ps.setString(22, wh_tax_cb);
    		ps.setDouble(23, Double.parseDouble(wh_tax_amount) / itemsCount);
    		ps.setString(24,DateManipulations.CalendarToDb(posting_date));
    		ps.setDate(25, dbConnection.dateConvert(depreciation_start_date));
    		ps.setString(26, reason);
    		ps.setInt(27, Integer.parseInt(location));
    		ps.setDouble(28, Double.parseDouble(vatable_cost) / itemsCount);
    		ps.setDouble(29, Double.parseDouble(vat_amount) / itemsCount);
    		ps.setString(30, require_depreciation);
    		ps.setString(31, subject_to_vat);
    		ps.setString(32, who_to_rem);
    		ps.setString(33, email_1);
    		ps.setString(34, who_to_rem_2);
    		ps.setString(35, email2);
    		ps.setInt(36, Integer.parseInt(state));
    		ps.setInt(37, Integer.parseInt(driver));
    		ps.setString(38, spare_1);
    		ps.setString(39, spare_2);
    		ps.setString(40, user_id);
    		ps.setInt(41, Integer.parseInt(province));
    		ps.setDate(42, dbConnection.dateConvert(warrantyStartDate));
    		ps.setInt(43, Integer.parseInt(noOfMonths));
    		ps.setDate(44, dbConnection.dateConvert(expiryDate));
    		ps.setString(45,code.getBranchCode(branch_id));
    		ps.setString(46,code.getDeptCode(department_id));
    		ps.setString(47,code.getSectionCode(section_id));
    		ps.setString(48,code.getCategoryCode(category_id));
    		ps.setLong(49, gid);
    		ps.setDouble(50, Double.parseDouble(amountPTD) / itemsCount);
    		ps.setDouble(51, ((Double.parseDouble(cost_price) / itemsCount)-(Double.parseDouble(amountPTD)/ itemsCount)));
    		ps.setDouble(52, accum_dep);
    		ps.setString(53,"N");
    		ps.setString(54,"Y");
            ps.setString(55,status);
            ps.setString(56,supervisor);
            ps.setString(57,lpo);
            ps.setString(58,bar_code);
            ps.setString(59,require_redistribution);
            ps.setString(60,"N");
            ps.setString(61,"N");
            ps.setString(62,"N");
            ps.setString(63, sbu_code);
            ps.setString(64, "P");
            ps.setString(65, invoiceNum);
            ps.setString(66, workstationIp);
    		ps.setString(67, spare_3);
    		ps.setString(68, spare_4);
    		ps.setString(69, spare_5);
    		ps.setString(70, spare_6);
    		ps.setString(71, sub_category_id);
    		ps.setString(72,code.getSubCategoryCode(sub_category_id));        
            boolean result = ps.execute();
                }
    catch (Exception r) {
    				System.out.println("INFO: Error creating group aset2 in archive in archiveAsset2Update>>" + r);
    			} 
            }

        	
        	public void getCreatedStockGroup(long id) {
            	dbConnection = new MagmaDBConnection();
            	
        		//String id = "";
        		String selectQuery = "SELECT group_id, quantity, Registration_No, " +
        				"Branch_ID,province, Dept_ID, Category_ID,Sub_Category_ID, section_id, Description, " +
        				"Vendor_AC, Date_purchased, dep_rate, asset_make, asset_model, " +
        				"asset_serial_no, asset_engine_no, supplier_name, asset_user, " +
        				"asset_maintenance, Cost_Price, dep_end_date, residual_value, " +
        				"authorized_by, wh_tax, wh_tax_amount, req_redistribution, " +
        				"Posting_Date, effective_date, purchase_reason, location, " +
        				"Vatable_Cost, Vat, Req_Depreciation, Subject_TO_Vat, " +
        				"Who_TO_Rem, email1, who_to_rem_2, email2, Raise_entry, " +
        				"dep_ytd, Section, Asset_Status, state, driver, spare_1, " +
        				"spare_2, spare_3,spare_4,spare_5,spare_6, user_ID,WAR_START_DATE, WAR_MONTH, " +
        				"WAR_EXPIRY_DATE, branch_code, dept_code, section_code, " +
        				"category_code,AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID,ACCUM_DEP," +
                         "LPO,BAR_CODE,defer_pay,SBU_CODE,Invoice_no,PROJECT_CODE  " +
        				" FROM am_group_Stock_main WHERE GROUP_ID="+id;

        		try {
        			Connection con = dbConnection.getConnection("legendPlus");
                    PreparedStatement ps = con.prepareStatement(selectQuery);
                    ResultSet rs = ps.executeQuery();
        			//rs = getStatement().executeQuery(selectQuery);
        			while(rs.next())
        				{
        				group_id =rs.getString("GROUP_ID");
        				no_of_items=rs.getString("quantity");
        				registration_no = rs.getString("REGISTRATION_NO");
                        branch_id = rs.getString("BRANCH_ID");
                        province = rs.getString("PROVINCE");
                        department_id = rs.getString("DEPT_ID");
                        category_id = rs.getString("CATEGORY_ID");
                        section_id = rs.getString("SECTION_ID");
                        description = rs.getString("DESCRIPTION");
                        vendor_account = rs.getString("VENDOR_AC");
                        depreciation_rate = rs.getString("DEP_RATE");
                        make = rs.getString("ASSET_MAKE");
                        model = rs.getString("ASSET_MODEL");
                        serial_number = rs.getString("ASSET_SERIAL_NO");
                        engine_number = rs.getString("ASSET_ENGINE_NO");
                        supplied_by = rs.getString("SUPPLIER_NAME");
                        authuser = rs.getString("ASSET_USER");
                        maintained_by = rs.getString("ASSET_MAINTENANCE");
                        cost_price = rs.getString("COST_PRICE");
                        depreciation_end_date = dbConnection.formatDate(rs.getDate("DEP_END_DATE"));
                        residual_value = rs.getString("RESIDUAL_VALUE");
                        authorized_by = rs.getString("AUTHORIZED_BY");
                        wh_tax_cb = rs.getString("WH_TAX");
                        wh_tax_amount = rs.getString("WH_TAX_AMOUNT");
                        require_redistribution = rs.getString("REQ_REDISTRIBUTION");
                        posting_date =DateManipulations.DateToCalendar(sdf.format(rs.getDate("POSTING_DATE")));
                        depreciation_start_date = dbConnection.formatDate(rs.getDate("EFFECTIVE_DATE"));
                        reason = rs.getString("PURCHASE_REASON");
                        location = rs.getString("LOCATION");
                        vatable_cost = rs.getString("VATABLE_COST");
                        vat_amount = rs.getString("VAT");
                        require_depreciation = rs.getString("REQ_DEPRECIATION");
                        subject_to_vat = rs.getString("SUBJECT_TO_VAT");
                        who_to_rem = rs.getString("WHO_TO_REM");
                        email_1 = rs.getString("EMAIL1");
                        who_to_rem_2 = rs.getString("WHO_TO_REM_2");
                        email2 = rs.getString("EMAIL2");
                        raise_entry = rs.getString("RAISE_ENTRY");
                        status = rs.getString("ASSET_STATUS");
                        state = rs.getString("STATE");
                        driver = rs.getString("DRIVER");
                        spare_1 = rs.getString("SPARE_1");
                        spare_2 = rs.getString("SPARE_2");
                        spare_3 = rs.getString("SPARE_3");
                        spare_4 = rs.getString("SPARE_4");
                        spare_5 = rs.getString("SPARE_5");
                        spare_6 = rs.getString("SPARE_6");
                        user = rs.getString("USER_ID");
                        warrantyStartDate = dbConnection.formatDate(rs.getDate(
                        "WAR_START_DATE"));
                        noOfMonths = rs.getString("WAR_MONTH");
                        expiryDate =  dbConnection.formatDate(rs.getDate("WAR_EXPIRY_DATE"));
                        amountPTD = rs.getString("AMOUNT_PTD");
                        amountREM = rs.getString("AMOUNT_REM");
                        partPAY = rs.getString("PART_PAY");
                        fullyPAID = rs.getString("FULLY_PAID");
                        accum_dep = rs.getDouble("ACCUM_DEP");
                        date_of_purchase =DateManipulations.DateToCalendar(sdf.format(rs.getDate("DATE_PURCHASED")));
                        lpo=rs.getString("LPO");
                        bar_code=rs.getString("BAR_CODE");
                        deferPay= rs.getString("defer_pay");
                        sbu_code= rs.getString("SBU_CODE");
                        //System.out.println("=====================================");
		        		invoiceNum= rs.getString("Invoice_no");
		        		projectCode= rs.getString("PROJECT_CODE");
                                }
        			//freeResource();

        		} catch (Exception er) {
        			System.out.println("INFO: Error fetching groupid >>" + er);
        			//id = "";
        		} 

        		//return id;
        	}


        	public String getCreatedDisposalGroupId(String category, String description) {
            	dbConnection = new MagmaDBConnection();
            	
        		String id = "";
        		String selectQuery = "SELECT ASSET_ID FROM AM_GROUP_DISPOSAL "
        				+ "WHERE CATEGORY_ID = " + category + " AND DESCRIPTION = '"
        				+ description + "'";

        		try {
        			Connection con = dbConnection.getConnection("legendPlus");
                    PreparedStatement ps = con.prepareStatement(selectQuery);
        			ResultSet rs = ps.executeQuery();
        			if (rs.next())
        			 {
        				id = rs.getString(1);
        			 }

        		} catch (Exception er) {
        			System.out.println("INFO: Error fetching groupid in getCreatedDisposalGroupId >>" + er);
        			id = "";
        		} 

        		return id;
        	}

        	
        	public void getCreatedDisposalGroup(long id) {
            	dbConnection = new MagmaDBConnection();
            	

                String selectQuery =
              			"SELECT disposal_ID,asset_id,disposalCost,disposal_reason,Disposal_Date,"+
      					"User_ID,raise_entry,buyer_ac,disposalAmount,"+
      					"vat_amount,wht_amount,nbv,accum_dep,cost_price,"+
      					"effDate,approval_status,WHT_PERCENT,WH_TAX,Subject_to_vat,"+
      					"branch_code,category_code,description,lpoNum,invoice_no,"+
      					"asset_code,INTEGRIFY,DISPOSED,PROJECT_CODE,LOCATION  "+
      					"FROM AM_GROUP_DISPOSAL WHERE DISPOSAL_ID="+id;
        		System.out.println("");
        		//System.out.println("========================================");
        		//System.out.println("Query in getCreatedGroup " + selectQuery);
        		//System.out.println("========================================");
        	//	System.out.println("getCreatedGroup selectQuery  "+selectQuery);
        		try {
        			Connection con = dbConnection.getConnection("legendPlus");
                    PreparedStatement ps = con.prepareStatement(selectQuery);
        			ResultSet rs = ps.executeQuery();
        			while(rs.next())
        				{
          				asset_id = rs.getString("ASSET_ID");
        				group_id =rs.getString("DISPOSAL_ID");
                        branch_id = rs.getString("BRANCH_CODE");
                        location = rs.getString("LOCATION");
                        category_id = rs.getString("CATEGORY_CODE");
                        description = rs.getString("DESCRIPTION");
                        vendor_account = rs.getString("buyer_ac");
                        cost_price = rs.getString("disposalCost");
                        wh_tax_cb = rs.getString("WH_TAX");
                        wh_tax_amount = rs.getString("wht_amount");
                        posting_date =DateManipulations.DateToCalendar(sdf.format(rs.getDate("DISPOSAL_DATE")));
                        depreciation_start_date = dbConnection.formatDate(rs.getDate("effDate"));
                        reason = rs.getString("disposal_reason");
                        location = rs.getString("LOCATION");
                        vatable_cost = rs.getString("disposalAmount");
                        vat_amount = rs.getString("vat_amount");
                        subject_to_vat = rs.getString("SUBJECT_TO_VAT");
                        user = rs.getString("USER_ID");
                        lpo=rs.getString("lpoNum");
		        		invoiceNum= rs.getString("Invoice_no");
		                projectCode= rs.getString("PROJECT_CODE");
		                integrifyId = rs.getString("INTEGRIFY");
                
               
                                }
        			//freeResource();
//        			dbConnection.closeConnection(con, ps, rs);
        		} catch (Exception er) {
        			System.out.println("INFO: Error fetching groupid in getCreatedDisposalGroup >>" + er);
        			//id = "";
        		} 

        		//return id;
        	}


public boolean saveGroupToAsset(String GroupId,String catRate) {
    boolean done = false;
   
    int rate = 100*12;
    int state = 1;
    int Dep_Rate = 0;   
    String memo = "N";  
    double RateValue =  0;
           if(catRate.equalsIgnoreCase("0.00")){catRate = "1";
    Dep_Rate = Integer.parseInt(catRate);
    RateValue = 0;
    }  
    else{
 	   double DepRate = Double.parseDouble(catRate);
 	   RateValue =(100/DepRate)*12;
    } 
       
    String FINDER_QUERY = "INSERT INTO AM_ASSET (ASSET_ID, REGISTRATION_NO, BRANCH_ID, DEPT_ID,SECTION_ID, CATEGORY_ID, DESCRIPTION,"
    		+ "VENDOR_AC,DATE_PURCHASED, DEP_RATE, ASSET_MAKE, ASSET_MODEL,ASSET_SERIAL_NO, ASSET_ENGINE_NO, SUPPLIER_NAME,"
    		+ "ASSET_USER, ASSET_MAINTENANCE, ACCUM_DEP, MONTHLY_DEP,COST_PRICE, NBV, DEP_END_DATE, RESIDUAL_VALUE,"
    		+ "AUTHORIZED_BY, POSTING_DATE, EFFECTIVE_DATE, PURCHASE_REASON,USEFUL_LIFE, TOTAL_LIFE, LOCATION, REMAINING_LIFE,"
    		+ "VATABLE_COST,VAT, WH_TAX, WH_TAX_AMOUNT, REQ_DEPRECIATION,REQ_REDISTRIBUTION, SUBJECT_TO_VAT, WHO_TO_REM, EMAIL1,WHO_TO_REM_2, "
    		+ "EMAIL2, RAISE_ENTRY, DEP_YTD, SECTION,STATE, DRIVER, SPARE_1, SPARE_2, ASSET_STATUS, USER_ID,MULTIPLE,PROVINCE, WAR_START_DATE, "
    		+ "WAR_MONTH,WAR_EXPIRY_DATE,LPO,BAR_CODE ,BRANCH_CODE,CATEGORY_CODE,GROUP_ID,PART_PAY,FULLY_PAID,DEFER_PAY,SBU_CODE,SECTION_CODE,"
    		+ "DEPT_CODE,system_ip,asset_code,memo,memoValue, SUB_CATEGORY_ID,SUB_CATEGORY_CODE, SPARE_3, SPARE_4, SPARE_5, SPARE_6,ZONE_CODE, "
    		+ "REGION_CODE,PROJECT_CODE,INTEGRIFY,TRANPORT_COST,OTHER_COST,VENDOR_NAME) SELECT ASSET_ID, REGISTRATION_NO, BRANCH_ID, DEPT_ID,SECTION_ID, CATEGORY_ID, DESCRIPTION,"
    		+ "VENDOR_AC,DATE_PURCHASED, DEP_RATE, ASSET_MAKE, ASSET_MODEL,ASSET_SERIAL_NO, ASSET_ENGINE_NO, SUPPLIER_NAME,ASSET_USER, "
    		+ "ASSET_MAINTENANCE, ACCUM_DEP, 0.00,COST_PRICE, COST_PRICE -10, DEP_END_DATE, RESIDUAL_VALUE,AUTHORIZED_BY, POSTING_DATE, "
    		+ "EFFECTIVE_DATE, PURCHASE_REASON,0,"+RateValue+", LOCATION, "+RateValue+",VATABLE_COST,VAT, WH_TAX, WH_TAX_AMOUNT, REQ_DEPRECIATION,"
    		+ "REQ_REDISTRIBUTION, SUBJECT_TO_VAT, WHO_TO_REM, EMAIL1,WHO_TO_REM_2, EMAIL2, RAISE_ENTRY, DEP_YTD, SECTION,STATE, DRIVER, "
    		+ "SPARE_1, SPARE_2, ASSET_STATUS, USER_ID,'N',PROVINCE, WAR_START_DATE, WAR_MONTH,WAR_EXPIRY_DATE,LPO,BAR_CODE ,BRANCH_CODE,"
    		+ "CATEGORY_CODE,GROUP_ID,PART_PAY,FULLY_PAID,DEFER_PAY,SBU_CODE,SECTION_CODE,DEPT_CODE,'127.0.0.1',asset_code,'N','', "
    		+ "SUB_CATEGORY_ID,SUB_CATEGORY_CODE, SPARE_3, SPARE_4, SPARE_5, SPARE_6,ZONE_CODE, REGION_CODE,PROJECT_CODE,INTEGRIFY,"
    		+ "TRANPORT_COST,OTHER_COST,VENDOR_NAME FROM AM_GROUP_ASSET WHERE Group_id = '"+GroupId+"'";
    
 String FINDER_QUERY2 = "INSERT INTO AM_ASSET_ARCHIVE (ASSET_ID, REGISTRATION_NO, BRANCH_ID, DEPT_ID,SECTION_ID, CATEGORY_ID, DESCRIPTION,"
 		+ "VENDOR_AC,DATE_PURCHASED, DEP_RATE, ASSET_MAKE, ASSET_MODEL,ASSET_SERIAL_NO, ASSET_ENGINE_NO, SUPPLIER_NAME,"
 		+ "ASSET_USER, ASSET_MAINTENANCE, ACCUM_DEP, MONTHLY_DEP,COST_PRICE, NBV, DEP_END_DATE, RESIDUAL_VALUE,"
 		+ "AUTHORIZED_BY, POSTING_DATE, EFFECTIVE_DATE, PURCHASE_REASON,USEFUL_LIFE, TOTAL_LIFE, LOCATION, REMAINING_LIFE,"
 		+ "VATABLE_COST,VAT, WH_TAX, WH_TAX_AMOUNT, REQ_DEPRECIATION,REQ_REDISTRIBUTION, SUBJECT_TO_VAT, WHO_TO_REM, EMAIL1,WHO_TO_REM_2, "
 		+ "EMAIL2, RAISE_ENTRY, DEP_YTD, SECTION,STATE, DRIVER, SPARE_1, SPARE_2, ASSET_STATUS, USER_ID,MULTIPLE,PROVINCE, WAR_START_DATE, "
 		+ "WAR_MONTH,WAR_EXPIRY_DATE,LPO,BAR_CODE ,BRANCH_CODE,CATEGORY_CODE,GROUP_ID,PART_PAY,FULLY_PAID,DEFER_PAY,SBU_CODE,SECTION_CODE,"
 		+ "DEPT_CODE,system_ip,asset_code,SUB_CATEGORY_ID,SUB_CATEGORY_CODE, SPARE_3, SPARE_4, SPARE_5, SPARE_6,ZONE_CODE, "
 		+ "REGION_CODE,PROJECT_CODE,INTEGRIFY,TRANPORT_COST,OTHER_COST,VENDOR_NAME) SELECT ASSET_ID, REGISTRATION_NO, BRANCH_ID, DEPT_ID,SECTION_ID, CATEGORY_ID, DESCRIPTION,"
 		+ "VENDOR_AC,DATE_PURCHASED, DEP_RATE, ASSET_MAKE, ASSET_MODEL,ASSET_SERIAL_NO, ASSET_ENGINE_NO, SUPPLIER_NAME,ASSET_USER, "
 		+ "ASSET_MAINTENANCE, ACCUM_DEP, 0.00,COST_PRICE, COST_PRICE -10, DEP_END_DATE, RESIDUAL_VALUE,AUTHORIZED_BY, POSTING_DATE, "
 		+ "EFFECTIVE_DATE, PURCHASE_REASON,0,"+RateValue+", LOCATION, "+RateValue+",VATABLE_COST,VAT, WH_TAX, WH_TAX_AMOUNT, REQ_DEPRECIATION,"
 		+ "REQ_REDISTRIBUTION, SUBJECT_TO_VAT, WHO_TO_REM, EMAIL1,WHO_TO_REM_2, EMAIL2, RAISE_ENTRY, DEP_YTD, SECTION,STATE, DRIVER, "
 		+ "SPARE_1, SPARE_2, ASSET_STATUS, USER_ID,'N',PROVINCE, WAR_START_DATE, WAR_MONTH,WAR_EXPIRY_DATE,LPO,BAR_CODE ,BRANCH_CODE,"
 		+ "CATEGORY_CODE,GROUP_ID,PART_PAY,FULLY_PAID,DEFER_PAY,SBU_CODE,SECTION_CODE,DEPT_CODE,'127.0.0.1',asset_code, "
 		+ "SUB_CATEGORY_ID,SUB_CATEGORY_CODE, SPARE_3, SPARE_4, SPARE_5, SPARE_6,ZONE_CODE, REGION_CODE,PROJECT_CODE,INTEGRIFY,"
 		+ "TRANPORT_COST,OTHER_COST,VENDOR_NAME FROM AM_GROUP_ASSET WHERE Group_id = '"+GroupId+"'";
 
//    System.out.println("saveGroupToAsset FINDER_QUERY === "+FINDER_QUERY);   
    try { 
    	Connection con = dbConnection.getConnection("legendPlus");
        PreparedStatement ps = con.prepareStatement(FINDER_QUERY);
        done = (ps.executeUpdate() != -1);
        PreparedStatement ps1 = con.prepareStatement(FINDER_QUERY2);
        done = (ps1.executeUpdate() != -1);
    } catch (Exception ex) {
        System.out.println("WARNING: cannot Post Group Asset records into  AM_ASSET 1--->"
                + ex.getMessage());
    } 

    return done;

}

public boolean saveGroupToUncapAsset(String GroupId) {
    boolean done = false;
    
    int rate = 100*12;
    int state = 1;
    int Dep_Rate = 0;   
    String memo = "N";  
    double RateValue =  0;
/*           if(catRate.equalsIgnoreCase("0.00")){catRate = "1";
    Dep_Rate = Integer.parseInt(catRate);
    RateValue = 0;
    }  
    else{
 	   double DepRate = Double.parseDouble(catRate);
 	   RateValue =(100/DepRate)*12;
    } */
       
    String FINDER_QUERY = "INSERT INTO AM_ASSET_UNCAPITALIZED (ASSET_ID, REGISTRATION_NO, BRANCH_ID, DEPT_ID,SECTION_ID, CATEGORY_ID, DESCRIPTION,"
    		+ "VENDOR_AC,DATE_PURCHASED, DEP_RATE, ASSET_MAKE, ASSET_MODEL,ASSET_SERIAL_NO, ASSET_ENGINE_NO, SUPPLIER_NAME,"
    		+ "ASSET_USER, ASSET_MAINTENANCE, ACCUM_DEP, MONTHLY_DEP,COST_PRICE, NBV, DEP_END_DATE, RESIDUAL_VALUE,"
    		+ "AUTHORIZED_BY, POSTING_DATE, EFFECTIVE_DATE, PURCHASE_REASON,USEFUL_LIFE, TOTAL_LIFE, LOCATION, REMAINING_LIFE,"
    		+ "VATABLE_COST,VAT, WH_TAX, WH_TAX_AMOUNT, REQ_DEPRECIATION,REQ_REDISTRIBUTION, SUBJECT_TO_VAT, WHO_TO_REM, EMAIL1,WHO_TO_REM_2, "
    		+ "EMAIL2, RAISE_ENTRY, DEP_YTD, SECTION,STATE, DRIVER, SPARE_1, SPARE_2, ASSET_STATUS, USER_ID,MULTIPLE,PROVINCE, WAR_START_DATE, "
    		+ "WAR_MONTH,WAR_EXPIRY_DATE,LPO,BAR_CODE ,BRANCH_CODE,CATEGORY_CODE,GROUP_ID,PART_PAY,FULLY_PAID,DEFER_PAY,SBU_CODE,SECTION_CODE,"
    		+ "DEPT_CODE,system_ip,asset_code,memo,memoValue, SUB_CATEGORY_ID,SUB_CATEGORY_CODE, SPARE_3, SPARE_4, SPARE_5, SPARE_6,ZONE_CODE, "
    		+ "REGION_CODE,PROJECT_CODE,INTEGRIFY,VENDOR_NAME) SELECT ASSET_ID, REGISTRATION_NO, BRANCH_ID, DEPT_ID,SECTION_ID, CATEGORY_ID, DESCRIPTION,"
    		+ "VENDOR_AC,DATE_PURCHASED, DEP_RATE, ASSET_MAKE, ASSET_MODEL,ASSET_SERIAL_NO, ASSET_ENGINE_NO, SUPPLIER_NAME,ASSET_USER, "
    		+ "ASSET_MAINTENANCE, ACCUM_DEP, 0.00,COST_PRICE, COST_PRICE -10, DEP_END_DATE, RESIDUAL_VALUE,AUTHORIZED_BY, POSTING_DATE, "
    		+ "EFFECTIVE_DATE, PURCHASE_REASON,0,0.00, LOCATION, 0.00,VATABLE_COST,VAT, WH_TAX, WH_TAX_AMOUNT, REQ_DEPRECIATION,"
    		+ "REQ_REDISTRIBUTION, SUBJECT_TO_VAT, WHO_TO_REM, EMAIL1,WHO_TO_REM_2, EMAIL2, RAISE_ENTRY, DEP_YTD, SECTION,STATE, DRIVER, "
    		+ "SPARE_1, SPARE_2, ASSET_STATUS, USER_ID,'N',PROVINCE, WAR_START_DATE, WAR_MONTH,WAR_EXPIRY_DATE,LPO,BAR_CODE ,BRANCH_CODE,"
    		+ "CATEGORY_CODE,GROUP_ID,PART_PAY,FULLY_PAID,DEFER_PAY,SBU_CODE,SECTION_CODE,DEPT_CODE,'127.0.0.1',asset_code,'N','', "
    		+ "SUB_CATEGORY_ID,SUB_CATEGORY_CODE, SPARE_3, SPARE_4, SPARE_5, SPARE_6,ZONE_CODE, REGION_CODE,PROJECT_CODE,INTEGRIFY,VENDOR_NAME"
    		+ " FROM AM_GROUP_ASSET_UNCAPITALIZED WHERE Group_id = '"+GroupId+"'";
    
 String FINDER_QUERY2 = "INSERT INTO AM_ASSET_ARCHIVE (ASSET_ID, REGISTRATION_NO, BRANCH_ID, DEPT_ID,SECTION_ID, CATEGORY_ID, DESCRIPTION,"
 		+ "VENDOR_AC,DATE_PURCHASED, DEP_RATE, ASSET_MAKE, ASSET_MODEL,ASSET_SERIAL_NO, ASSET_ENGINE_NO, SUPPLIER_NAME,"
 		+ "ASSET_USER, ASSET_MAINTENANCE, ACCUM_DEP, MONTHLY_DEP,COST_PRICE, NBV, DEP_END_DATE, RESIDUAL_VALUE,"
 		+ "AUTHORIZED_BY, POSTING_DATE, EFFECTIVE_DATE, PURCHASE_REASON,USEFUL_LIFE, TOTAL_LIFE, LOCATION, REMAINING_LIFE,"
 		+ "VATABLE_COST,VAT, WH_TAX, WH_TAX_AMOUNT, REQ_DEPRECIATION,REQ_REDISTRIBUTION, SUBJECT_TO_VAT, WHO_TO_REM, EMAIL1,WHO_TO_REM_2, "
 		+ "EMAIL2, RAISE_ENTRY, DEP_YTD, SECTION,STATE, DRIVER, SPARE_1, SPARE_2, ASSET_STATUS, USER_ID,MULTIPLE,PROVINCE, WAR_START_DATE, "
 		+ "WAR_MONTH,WAR_EXPIRY_DATE,LPO,BAR_CODE ,BRANCH_CODE,CATEGORY_CODE,GROUP_ID,PART_PAY,FULLY_PAID,DEFER_PAY,SBU_CODE,SECTION_CODE,"
 		+ "DEPT_CODE,system_ip,asset_code,SUB_CATEGORY_ID,SUB_CATEGORY_CODE, SPARE_3, SPARE_4, SPARE_5, SPARE_6,ZONE_CODE, "
 		+ "REGION_CODE,PROJECT_CODE,INTEGRIFY,VENDOR_NAME) SELECT ASSET_ID, REGISTRATION_NO, BRANCH_ID, DEPT_ID,SECTION_ID, CATEGORY_ID, DESCRIPTION,"
 		+ "VENDOR_AC,DATE_PURCHASED, DEP_RATE, ASSET_MAKE, ASSET_MODEL,ASSET_SERIAL_NO, ASSET_ENGINE_NO, SUPPLIER_NAME,ASSET_USER, "
 		+ "ASSET_MAINTENANCE, ACCUM_DEP, 0.00,COST_PRICE, COST_PRICE -10, DEP_END_DATE, RESIDUAL_VALUE,AUTHORIZED_BY, POSTING_DATE, "
 		+ "EFFECTIVE_DATE, PURCHASE_REASON,0,0.00, LOCATION, 0.00,VATABLE_COST,VAT, WH_TAX, WH_TAX_AMOUNT, REQ_DEPRECIATION,"
 		+ "REQ_REDISTRIBUTION, SUBJECT_TO_VAT, WHO_TO_REM, EMAIL1,WHO_TO_REM_2, EMAIL2, RAISE_ENTRY, DEP_YTD, SECTION,STATE, DRIVER, "
 		+ "SPARE_1, SPARE_2, ASSET_STATUS, USER_ID,'N',PROVINCE, WAR_START_DATE, WAR_MONTH,WAR_EXPIRY_DATE,LPO,BAR_CODE ,BRANCH_CODE,"
 		+ "CATEGORY_CODE,GROUP_ID,PART_PAY,FULLY_PAID,DEFER_PAY,SBU_CODE,SECTION_CODE,DEPT_CODE,'127.0.0.1',asset_code, "
 		+ "SUB_CATEGORY_ID,SUB_CATEGORY_CODE, SPARE_3, SPARE_4, SPARE_5, SPARE_6,ZONE_CODE, REGION_CODE,PROJECT_CODE,INTEGRIFY,VENDOR_NAME"
 		+ " FROM AM_GROUP_ASSET_UNCAPITALIZED WHERE Group_id = '"+GroupId+"'";
 
//    System.out.println("saveGroupToUncapAsset FINDER_QUERY === "+FINDER_QUERY);   
    try { 
    	Connection con = dbConnection.getConnection("legendPlus");
        PreparedStatement ps = con.prepareStatement(FINDER_QUERY);
        done = (ps.executeUpdate() != -1);
        PreparedStatement ps1 = con.prepareStatement(FINDER_QUERY2);
        done = (ps1.executeUpdate() != -1);
    } catch (Exception ex) {
        System.out.println("WARNING: cannot Post Group Asset records into  AM_ASSET--->"
                + ex.getMessage());
    } 

    return done;

}


public java.util.ArrayList getAsstCategoryIdOld(String GroupId)
{ 
	java.util.ArrayList _list = new java.util.ArrayList();
	String finacleTransId= null;
		String query = "SELECT a.Category_ID,c.dep_rate FROM AM_GROUP_ASSET a, am_ad_category c "+ 
	    "WHERE a.Category_ID = c.Category_ID and a.Group_id = '"+GroupId+"' GROUP BY a.Category_ID,c.Dep_rate";

	 

	try {
		Connection c = dbConnection.getConnection("legendPlus");
        PreparedStatement ps = c.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
			while (rs.next())
			   {				
				int strid = rs.getInt("Category_ID");
				double depRate = rs.getDouble("dep_rate");
				ViewAssetDetails asset = new ViewAssetDetails();
				asset.setId(strid);
				asset.setDepRate(depRate);
				_list.add(asset);
			   }
	 }   
				 catch (Exception e)
					{
						e.printStackTrace();
					}
					
	return _list;
} 

public java.util.ArrayList getAsstCategoryId(String groupId) {

	java.util.ArrayList list = new java.util.ArrayList();

    String query =
        "SELECT a.Category_ID, c.dep_rate " +
        "FROM AM_GROUP_ASSET a " +
        "JOIN am_ad_category c ON a.Category_ID = c.Category_ID " +
        "WHERE a.Group_id = ? " +
        "GROUP BY a.Category_ID, c.dep_rate";

    try (Connection c = dbConnection.getConnection("legendPlus");
         PreparedStatement ps = c.prepareStatement(query)) {

        ps.setString(1, groupId);

        try (ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                ViewAssetDetails asset = new ViewAssetDetails();
                asset.setId(rs.getInt("Category_ID"));
                asset.setDepRate(rs.getDouble("dep_rate"));

                list.add(asset);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}

public java.util.ArrayList getUncapAsstCategoryId(String GroupId)
{ 
	java.util.ArrayList _list = new java.util.ArrayList();
	String finacleTransId= null;
		String query = "SELECT a.Category_ID,c.dep_rate FROM AM_GROUP_ASSET_UNCAPITALIZED a, am_ad_category c "+ 
	    "WHERE a.Category_ID = c.Category_ID and a.Group_id = '"+GroupId+"' GROUP BY a.Category_ID,c.Dep_rate";

	

	try {
		Connection c = dbConnection.getConnection("legendPlus");
        PreparedStatement ps = c.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
			while (rs.next())
			   {				
				int strid = rs.getInt("Category_ID");
				double depRate = rs.getDouble("dep_rate");
				ViewAssetDetails asset = new ViewAssetDetails();
				asset.setId(strid);
				asset.setDepRate(depRate);
				_list.add(asset);
			   }
	 }   
				 catch (Exception e)
					{
						e.printStackTrace();
					}
					
	return _list;
} 
	   	

public java.util.ArrayList getAsstIdGeneration(String GroupId)
{ 
	java.util.ArrayList _list = new java.util.ArrayList();
	String finacleTransId= null;
		String query = "SELECT Category_ID,ID,Branch_ID,Dept_ID,section_id,user_ID FROM AM_GROUP_ASSET WHERE Group_id = '"+GroupId+"' ";



	try {
		Connection c = dbConnection.getConnection("legendPlus");
        PreparedStatement ps = c.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
			while (rs.next())
			   {				
				String strid = rs.getString("ID");
//				  System.out.println("getAsstIdGeneration strid === "+strid);  
				String cateId = rs.getString("Category_ID");
				String branchId = rs.getString("Branch_ID");
				String deptId = rs.getString("Dept_ID");
				String sectId = rs.getString("section_id");
				int userId = rs.getInt("user_ID");
				Asset asset = new Asset();
				asset.setId(strid);
				asset.setCategory(cateId);
				asset.setBranchId(branchId);
				asset.setDepartmentId(deptId);
				asset.setSection(sectId);
				asset.setUserid(userId);
				_list.add(asset);
			   }
	 }   
				 catch (Exception e)
					{
						e.printStackTrace();
					}
					
	return _list;
} 

public java.util.ArrayList getUncapAsstIdGeneration(String GroupId)
{ 
	java.util.ArrayList _list = new java.util.ArrayList();
	String finacleTransId= null;
		String query = "SELECT Category_ID,ID,Branch_ID,Dept_ID,section_id,user_ID FROM AM_GROUP_ASSET_UNCAPITALIZED WHERE Group_id = '"+GroupId+"' ";

	

	try {
		Connection c = dbConnection.getConnection("legendPlus");
        PreparedStatement ps = c.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
			while (rs.next())
			   {				
				String strid = rs.getString("ID");
//				  System.out.println("getUncapAsstIdGeneration strid === "+strid);  
				String cateId = rs.getString("Category_ID");
				String branchId = rs.getString("Branch_ID");
				String deptId = rs.getString("Dept_ID");
				String sectId = rs.getString("section_id");
				int userId = rs.getInt("user_ID");
				Asset asset = new Asset();
				asset.setId(strid);
				asset.setCategory(cateId);
				asset.setBranchId(branchId);
				asset.setDepartmentId(deptId);
				asset.setSection(sectId);
				asset.setUserid(userId);
				_list.add(asset);
			   }
	 }   
				 catch (Exception e)
					{
						e.printStackTrace();
					}
					
	return _list;
} 

public void groupAssetRateGen(String groupId, int catId, double depRate) {
      
       String NOTIFY_QUERY ="UPDATE AM_GROUP_ASSET SET dep_rate = ? WHERE Group_id = ? and Category_ID = ? ";

       try {
    	   Connection con = dbConnection.getConnection("legendPlus");
           PreparedStatement ps = con.prepareStatement(NOTIFY_QUERY);
           ps.setDouble(1, depRate);
           ps.setString(2, groupId);
           ps.setInt(3, catId);
           ps.executeUpdate();

       } catch (Exception ex) {
           System.out.println("WARNING: cannot update am_group_asset : "+ex.getMessage());
       } 

   }

public void groupUncpAssetRateGen(String groupId, int catId, double depRate) {
       
       String NOTIFY_QUERY ="UPDATE AM_GROUP_ASSET_UNCAPITALIZED SET dep_rate = ? WHERE Group_id = ? and Category_ID = ? ";

       try {
    	   Connection con = dbConnection.getConnection("legendPlus");
           PreparedStatement ps = con.prepareStatement(NOTIFY_QUERY);
           ps.setDouble(1, depRate);
           ps.setString(2, groupId);
           ps.setInt(3, catId);
           ps.executeUpdate();

       } catch (Exception ex) {
           System.out.println("WARNING: cannot update AM_GROUP_ASSET_UNCAPITALIZED : "+ex.getMessage());
       } 

   }

public void assetIdandCodeGen(String groupId, int Id, String assetId,int assetCode) {
       
       String NOTIFY_QUERY ="UPDATE AM_GROUP_ASSET SET Asset_Id = ?, asset_code = ? WHERE Group_id = ? and Id = ? ";
//       System.out.println("<<<<<<<<<<<<groupId in assetIdandCodeGen: "+groupId+"    <<<<Id: "+Id+"    <<<<assetId: "+assetId+"    <<<<assetCode: "+assetCode);
       try {
    	   Connection con = dbConnection.getConnection("legendPlus");
           PreparedStatement ps = con.prepareStatement(NOTIFY_QUERY);
           ps.setString(1, assetId);
           ps.setInt(2, assetCode); 
           ps.setString(3, groupId);
           ps.setInt(4, Id);
           ps.executeUpdate();

       } catch (Exception ex) {
           System.out.println("WARNING: cannot update am_group_asset in assetIdandCodeGen: "+ex.getMessage());
       } 

   }

public void UncapAssetIdandCodeGen(String groupId, int Id, String assetId,int assetCode) {
       
       String NOTIFY_QUERY ="UPDATE AM_GROUP_ASSET_UNCAPITALIZED SET Asset_Id = ?, asset_code = ? WHERE Group_id = ? and Id = ? ";
//       System.out.println("<<<<<<<<<<<<groupId in uncapAssetIdandCodeGen: "+groupId+"    <<<<Id: "+Id+"    <<<<assetId: "+assetId+"    <<<<assetCode: "+assetCode);
       try {
    	   Connection con = dbConnection.getConnection("legendPlus");
           PreparedStatement ps = con.prepareStatement(NOTIFY_QUERY);
           ps.setString(1, assetId);
           ps.setInt(2, assetCode);
           ps.setString(3, groupId);
           ps.setInt(4, Id); 
           ps.executeUpdate();

       } catch (Exception ex) {
           System.out.println("WARNING: cannot update AM_GROUP_ASSET_UNCAPITALIZED in UncapAssetIdandCodeGen: "+ex.getMessage());
       } 

   } 

public void changeGroupAssetStatus(String id,String status) 
{
	// TODO Auto-generated method stub
	
    String query_r ="update am_group_asset set asset_status=? " +
	" where Group_id = '"+id+"'";

        String query_archive="update am_group_asset_archive set asset_status=? " +
	" where Group_id = '"+id+"'";
    try 
    	{
    	Connection con = dbConnection.getConnection("legendPlus");
        PreparedStatement ps = con.prepareStatement(query_r);
    	ps.setString(1,status);
       	int i =ps.executeUpdate();

        PreparedStatement ps2 = con.prepareStatement(query_archive);
    	ps2.setString(1,status);
       	i =ps2.executeUpdate();

        changeGroupAssetMainStatus(id,status);
        } 
	catch (Exception ex)
	    {
	        System.out.println("changeGroupAssetStatus in GroupAssetToAssetBean: Error Updating am_group_asset " + ex);
	    } 
	
}


public void changeGroupAssetMainStatus(String id, String status2)
{
	// TODO Auto-generated method stub
	String query_r ="update am_group_asset_main set asset_status=? " +
	"where Group_id = '"+id+"'";

            String query_archive ="update am_group_asset_main_archive set asset_status=? " +
	"where Group_id = '"+id+"'";
            
	
    try 
	{
    	Connection con = dbConnection.getConnection("legendPlus");
        PreparedStatement ps = con.prepareStatement(query_r);
	ps.setString(1,status2);
    int i =ps.executeUpdate();

       PreparedStatement ps2 = con.prepareStatement(query_archive);
	ps2.setString(1,status2);
     i =ps2.executeUpdate();

    } 
    catch (Exception ex)
    {
        System.out.println("changeGroupAssetMainStatus in GroupAssetToAssetBean: Error Updating am_group_asset_main : " + ex);
    } 
   
}

private boolean checkApprovalStatus(String code)
{
	// TODO Auto-generated method stub
	boolean status = false;
	String approval_status_qry = "select level from approval_level_setup where code ='"+code+"'";
	
    int level = 0;
    try
    {
    	Connection con = dbConnection.getConnection("legendPlus");
        PreparedStatement ps = con.prepareStatement(approval_status_qry);
    	ResultSet rs = ps.executeQuery();
    	if(rs.next())
    	{
    		level= rs.getInt(1);
    	}
    	if (level > 0)
    	{
    		status = true;
    	}
    }
    catch(Exception ex) 
    {
        System.out.println("WARN: Error checkApprovalStatus ->" + ex);
    }
      
    return status;
}

public String setGroupPendingMultipleTrans(String[] a, String code,int assetCode,String supervisorId,String mtid){

int transaction_level=0;
String transaction_type = "";

String pq =
"insert into am_asset_approval(asset_id,user_id,super_id,amount,posting_date,description," +
"effective_date,branchCode,asset_status,tran_type, process_status,tran_sent_time," +
"transaction_id,batch_id,transaction_level,asset_code) " +
"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
String tranLevelQuery = "select level,Transaction_type from approval_level_setup where code ='"+code+"'";

try
{ 
	Connection con = dbConnection.getConnection("legendPlus");
    PreparedStatement ps = con.prepareStatement(tranLevelQuery);
  ResultSet rs = ps.executeQuery();
while(rs.next()){
transaction_level = rs.getInt(1);
transaction_type = rs.getString(2);
}
ps = con.prepareStatement(pq);

SimpleDateFormat timer = new SimpleDateFormat("kk:mm:ss");

mtid =  new ApplicationHelper().getGeneratedId("am_asset_approval");

ps.setString(1, (a[0]==null)?"":a[0]);
ps.setString(2, (a[1]==null)?"":a[1]);
//ps.setString(3, (a[2]==null)?"":a[2]);
ps.setString(3, supervisorId);
ps.setDouble(4, (a[3]==null)?0:Double.parseDouble(a[3]));
//System.out.println("Posting_Date b4 conversion in setGroupPendingMultipleTrans : " +  a[4]);
ps.setDate(5, (a[4])==null?null:dbConnection.dateConvert(a[4]));
//System.out.println("Posting_Date after setGroupPendingMultipleTrans : " +  a[4]);
ps.setString(6, (a[5]==null)?"":a[5]);
// System.out.println("effective_date b4 conversion in setGroupPendingMultipleTrans : " +  a[6]);
ps.setDate(7,(a[6])==null?null:dbConnection.dateConvert(a[6]));
//System.out.println("effective_date after conversion in setGroupPendingMultipleTrans : " +  a[6]);
ps.setString(8, (a[7]==null)?"":a[7]);
ps.setString(9, (a[8]==null)?"":a[8]); //asset_status
ps.setString(10, transaction_type); //Transaction Type
//ps.setString(10, (a[9]==null)?"":a[9]);
ps.setString(11, a[10]);
ps.setString(12, timer.format(new java.util.Date()));
ps.setString(13,mtid);
ps.setString(14, mtid);
ps.setInt(15, transaction_level);
ps.setInt(16, assetCode);
ps.execute();

}
catch(Exception er)
{
System.out.println(">>>GroupAssetBean:setGroupPendingTrans(>>>>>>" + er);

}
return mtid;
}


public void deleteOtherSupervisors(String batchId, String supervisorId){
	String query_del ="DELETE FROM am_asset_approval WHERE Asset_Id = '"+batchId+"' and super_id != '"+supervisorId+"' and Asset_Status = 'PENDING' ";
//	System.out.println("query_r deleteOtherSupervisors>>>> "+query_del);
	
	try {
		Connection con = dbConnection.getConnection("legendPlus");
        PreparedStatement ps = con.prepareStatement(query_del);
	           int j =ps.executeUpdate();	           
	        } catch (Exception ex) {

	            System.out.println("AssetRecordBean: deleteOtherSupervisors()>>>>>" + ex);
	        } 


	}

public Connection getConnectionOld(String jndi) {
    Connection con = null;
    try {  
        Context initContext = new InitialContext();
        String dsJndi = "java:/legendPlus";

        DataSource ds = (DataSource) initContext.lookup(
        		dsJndi);
        con = ds.getConnection();

    } catch (Exception e) {
        System.out.println("WARNING:Error closing Connection Parameter Jndi ->" +
                           e.getMessage());
    }

    return con;
}


public Connection getConnection(String jndi) {
    try {
        Context ctx = new InitialContext();
        String dsJndi = "java:/legendPlus";
        DataSource ds = (DataSource) ctx.lookup(dsJndi);
        return ds.getConnection();
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

public void closeConnection(Connection con, PreparedStatement ps,
        ResultSet rs) {
try {
if (rs != null) {
rs.close();
}
if (ps != null) {
ps.close();
}
if (con != null) {
con.close();
}
} catch (Exception e) {
System.out.println("WARNING:Error closing Parameter 0 Connection ->" +
       e.getMessage());
}

}  


public void closeConnection(Connection con, Statement ps,
           ResultSet rs) {
try {
if (rs != null) {
rs.close();
}
if (ps != null) {
ps.close();
}
if (con != null) {
con.close();
}
} catch (Exception e) {
System.out.println("WARNING:Error closing Connection Parameter Con,ps,rs ->" +
          e.getMessage());
}

}

public void closeConnection(ResultSet rs) {
try {
System.out.println("Display Connections: "+rs);
if (rs != null) {
rs.close();
}
} catch (Exception e) {
System.out.println("WARNING:Error closing Connection Parameter Rs ->" +
          e.getMessage());
}

}

public void closeConnection(Connection con) {
try {
if (con != null) {
con.close();
}
} catch (Exception e) {
System.out.println("WARNING:Error closing Connection Parameter Con->" +
       e.getMessage());
}

}
	
}
