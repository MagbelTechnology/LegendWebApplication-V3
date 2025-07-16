package magma;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import legend.ConnectionClass;
import magma.net.dao.MagmaDBConnection;
import magma.util.Codes;

import com.magbel.legend.bus.ApprovalRecords;
import com.magbel.util.ApplicationHelper;
import com.magbel.util.DatetimeFormat;
import com.magbel.util.HtmlUtility;
// Referenced classes of package magma:
//            DateManipulations

public class ExcelAssetBidBean extends ConnectionClass
{
	public ApprovalRecords approve;
	public HtmlUtility htmlUtil;	
    private String branch_id;
    private String department_id;
    private String section_id;
    private String category_id;
    private String subcategory_id;
    private String make;
    private String location;
    private String asset_id;
    private String asset_code;
    private String maintained_by;
    private String driver;
    private String state;
    private String supplied_by;
    private String registration_no;
    private Calendar date_of_purchase;
    private Calendar depreciation_start_date;
    private Calendar depreciation_end_date;
    private String authorized_by;
    private String reason;
    private String no_of_items;
    private String description;
    private String cost_price;
    private String vatable_cost;
    private String subject_to_vat;
    private String vat_amount;
    private String wh_tax_cb;
    private String wh_tax_amount;
    private String serial_number;
    private String engine_number;
    private String model;
    private String user;
    private String depreciation_rate;
    private String residual_value;
    private String require_depreciation;
    private String vendor_account;
    private String spare_1;
    private String spare_2;
    private String spare_3;
    private String spare_4;
    private String spare_5;
    private String spare_6;
    private String who_to_rem;
    private String email_1;
    private String who_to_rem_2;
    private String email2;
    private String user_id;
    private Calendar posting_date;
    private String province;
    private Calendar warrantyStartDate;
    private String noOfMonths;
    private String raise_entry;
    private String gid;
    private Calendar expiryDate;
    SimpleDateFormat sdf;
    private DatetimeFormat dateFormat;
    private MagmaDBConnection dbConnection;
    private String amountPTD;
    private String amountREM;
    private String partPAY;
    private String fullyPAID;
    private String Assetid;
    private char processflag;
    private char postflag;
    private String serial_no;
    private String invoice_No;
    private String lpo_no;
    private String bar_code;
    private String wh_tax;
    private String branch_code;
    private String department_code;
    private String category_code;
    private String subcategory_code;
    private String section_code;
    private String sbu_code;
    private String assetuser;
    private String serialNo;
    private String vendor;
    private String assetMaintainBy;
    private String purchaseReason;
    private String projectCode;
    private String regionCode;
    private String zoneCode;
    private String locationCode;
    public ExcelAssetBidBean()
        throws Exception
    {
        branch_id = "0";
        department_id = "0";
        section_id = "0";
        category_id = "0";
        subcategory_id = "0";
        make = "0";
        location = "0";
        maintained_by = "0";
        driver = "0";
        state = "0";
        supplied_by = "0";
        registration_no = "";
        date_of_purchase = new GregorianCalendar();
        depreciation_start_date = new GregorianCalendar();
        depreciation_end_date = new GregorianCalendar();
        authorized_by = "";
        reason = "";        
        no_of_items = "0";
        description = "";
        cost_price = "0";
        vatable_cost = "0";
        subject_to_vat = "N";
        vat_amount = "0";
        wh_tax_cb = "N";
        wh_tax_amount = "0";
        serial_number = "";
        engine_number = "";
        model = "";
        user = "";
        depreciation_rate = "100";
        residual_value = getResidualvalue();
        require_depreciation = "N";
        vendor_account = "";
        spare_1 = "";
        spare_2 = "";
        spare_3 = "";
        spare_4 = "";
        spare_5 = "";
        spare_6 = "";
        who_to_rem = "";
        email_1 = "";
        who_to_rem_2 = "";
        email2 = "";
        user_id = "0";
        posting_date = new GregorianCalendar();
        province = "";
        warrantyStartDate = new GregorianCalendar();
        noOfMonths = "";
        gid = "1";
        expiryDate = new GregorianCalendar();
        partPAY = "N";
        fullyPAID = "N";
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        dbConnection = new MagmaDBConnection();
        dateFormat = new DatetimeFormat();
        processflag ='N';
        postflag = 'P';
        serial_no = "";
        invoice_No = "";
        lpo_no = "";
        bar_code = "";
        wh_tax = "";
        branch_code = "";
        department_code = "";
        category_code = "";
        section_code = "";
        sbu_code = "";
        locationCode = "";
        assetuser = "";
        approve = new ApprovalRecords(); 
        htmlUtil = new HtmlUtility(); 
        purchaseReason = "";
        projectCode = "";
        regionCode = "";
    	zoneCode = "";
//    	assetMaintainBy = "";

    }

    public void createGroup()
        throws Exception
    {
        StringBuffer b;
        b = new StringBuffer(400);
        Codes code = new Codes();
        if(no_of_items == null || no_of_items.equals(""))
        {
            no_of_items = "0";
        }
        if(province == null || province.equals(""))
        {
            province = "0";
        }
        if(noOfMonths == null || noOfMonths.equals(""))
        {
            noOfMonths = "0";
        }
        if(warrantyStartDate == null || warrantyStartDate.equals(""))
        {
            warrantyStartDate = null;
        }
        if(expiryDate == null || expiryDate.equals(""))
        {
            expiryDate = null;
        }
        require_depreciation = "N";
        String query = "INSERT INTO AM_GROUP_ASSET_BID(REGISTRATION_NO,BRANCH_ID,DEPT_ID,CATEGORY_ID,SUB_CATEGORY_ID,SECTION" +
"_ID,DESCRIPTION,VENDOR_AC,DATE_PURCHASED,DEP_RATE,ASSET_MAKE,ASSET_MODEL,ASSET_S" +
"ERIAL_NO,ASSET_ENGINE_NO,SUPPLIER_NAME,ASSET_USER,ASSET_MAINTENANCE,COST_PRICE,D" +
"EP_END_DATE,RESIDUAL_VALUE,AUTHORIZED_BY,WH_TAX,WH_TAX_AMOUNT,POSTING_DATE,EFFEC" +
"TIVE_DATE,PURCHASE_REASON,LOCATION,VATABLE_COST,VAT,REQ_DEPRECIATION,SUBJECT_TO_" +
"VAT,WHO_TO_REM,\tEMAIL1,\tWHO_TO_REM_2,EMAIL2,\tSTATE,DRIVER,SPARE_1,SPARE_2,SPARE_3,SPARE_4,SPARE_5,SPARE_6,[US" +
"ER_ID], PROVINCE, WAR_START_DATE, WAR_MONTH, WAR_EXPIRY_DATE,BRANCH_CODE,DEPT_CO" +
"DE,SECTION_CODE,CATEGORY_CODE,SUB_CATEGORY_CODE,GROUP_ID,AMOUNT_PTD,AMOUNT_REM,PART_PAY,FULLY_PAID,SBU_CODE," +
"PROJECT_CODE,REGION_CODE,ZONE_CODE" +
" )\tVALUES('";
        
        b.append(query);
        b.append(registration_no);
        b.append("',");
        b.append(branch_id);
        b.append(",");
        b.append(department_id);
        b.append(",");
        b.append(category_id);
        b.append(",");
        b.append(subcategory_id);
        b.append(",");
        b.append(section_id);
        b.append(",'");
        b.append(description);
        b.append("','");
        b.append(vendor_account);
        b.append("','");
        System.out.println(DateManipulations.CalendarToDb(date_of_purchase));
        b.append(DateManipulations.CalendarToDb(date_of_purchase));
        b.append("',");
        b.append(depreciation_rate);
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
        b.append(assetuser);
        b.append("',");
        b.append(maintained_by);
        b.append(",");
        b.append(Double.parseDouble(cost_price));
        b.append(",'");
        b.append(DateManipulations.CalendarToDb(depreciation_end_date));
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
        b.append(DateManipulations.CalendarToDb(date_of_purchase));
        b.append("',");
        b.append(purchaseReason);
        b.append(",");
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
        b.append(spare_3);
        b.append("','");
        b.append(spare_4);
        b.append("',");
        b.append(spare_5);
        b.append("','");
        b.append(spare_6);
        b.append("',");
        b.append((new StringBuilder(String.valueOf(user_id))).append(",").toString());
        b.append(province);
        b.append(",'");
        b.append(DateManipulations.CalendarToDb(warrantyStartDate));
        b.append("',");
        b.append(noOfMonths);
        b.append(",'");
        b.append(DateManipulations.CalendarToDb(expiryDate));
        b.append("','");
        b.append(code.getBranchCode(branch_id));
        b.append("','");
        b.append(code.getDeptCode(department_id));
        b.append("','");
        b.append(code.getSectionCode(section_id));
        b.append("','"); 
        b.append(code.getCategoryCode(category_id));
        b.append("',");
        b.append(code.getSubCategoryCode(subcategory_id));
        b.append("',");        
        b.append(gid);
        b.append(",");
        b.append(Double.parseDouble(cost_price));
        b.append(",");
        b.append(0);
        b.append(",'");
        b.append(partPAY);
        b.append("','");
        b.append(fullyPAID);
        b.append("','");
        b.append(sbu_code);
        b.append("','");
        b.append(projectCode);
        b.append("','");
        b.append(regionCode);
        b.append("','");
        b.append(zoneCode);
        
 /*       b.append("','");
        b.append(purchaseReason);*/
        b.append("')");
        try
        {   
            getStatement().executeUpdate(b.toString());
        }
        catch(Exception r)
        {
            System.out.println((new StringBuilder("INFO: Error creating group aset >>")).append(r).toString());
        }

    }

public void createGroupUpload(String asset_id, String gid,String asset_code)
throws Exception
{
	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	String id = "";
	
	Assetid = asset_id;
StringBuffer b;
b = new StringBuffer(400);
Codes code = new Codes();
if(no_of_items == null || no_of_items.equals(""))
{
    no_of_items = "0";
}
if(province == null || province.equals(""))
{
    province = "0";
}
if(noOfMonths == null || noOfMonths.equals(""))
{
    noOfMonths = "0";
}
if(warrantyStartDate == null || warrantyStartDate.equals(""))
{
    warrantyStartDate = null;
}
if(expiryDate == null || expiryDate.equals(""))
{
    expiryDate = null;
}    
//System.out.println("=====createGroupUpload asset_id=====> "+asset_id);
//System.out.println("=====createGroupUpload description=====> "+description);
String query = "INSERT INTO AM_GROUP_ASSET_BID(ASSET_ID,BRANCH_ID,DEPT_ID,CATEGORY_ID,SECTION" +
"_ID,DESCRIPTION,DATE_PURCHASED,COST_PRICE,WH_TAX_AMOUNT,POSTING_DATE,LOCATION," +
"VATABLE_COST,VAT,USER_ID,BRANCH_CODE,DEPT_CODE,SECTION_CODE,CATEGORY_CODE,GROUP_ID,"+
"ASSET_STATUS,SBU_CODE,ASSET_CODE,DEP_RATE,POST_FLAG" +
" )VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

try {   
	con = getConnection();  
	ps = con.prepareStatement(query);
	ps.setString(1, Assetid);
	ps.setInt(2, Integer.parseInt(branch_id));
	ps.setInt(3, Integer.parseInt(department_id));
	ps.setInt(4, Integer.parseInt(category_id));
	ps.setInt(5, Integer.parseInt(section_id));
	ps.setString(6, description);
	ps.setString(7, DateManipulations.CalendarToDb(date_of_purchase));
	ps.setDouble(8, Double.parseDouble(cost_price));
	ps.setDouble(9, Double.parseDouble(wh_tax_amount));
	ps.setString(10, DateManipulations.CalendarToDb(posting_date));
	ps.setInt(11, Integer.parseInt(location));
	ps.setDouble(12, Double.parseDouble(vatable_cost));
	ps.setDouble(13, Double.parseDouble(vat_amount));
	ps.setInt(14, Integer.parseInt(user_id));
	ps.setString(15, branch_code);
	ps.setString(16, department_code);
	ps.setString(17, section_code);
	ps.setString(18, category_code);
	ps.setString(19, gid);
	ps.setString(20, "PENDING");
	ps.setString(21, sbu_code);
	ps.setString(22, asset_code);
	ps.setDouble(23, 0.00);
	ps.setString(24, "P");
done = (ps.executeUpdate() != -1);
//System.out.println("=====query=====> "+query);
if(done==true){
	id ="SUCCESS";
}else{
	id ="FAILED";
}

} catch (Exception e) {

e.printStackTrace();
System.out.println(this.getClass().getName()
	+ " INFO: Error creating group aset >> "
		+ e.getMessage());
} finally {
	dbConnection.closeConnection(con, ps);
}

}



    public void setBranch_id(String branch_id)
    {
        if(branch_id != null)
        {
            this.branch_id = branch_id;
        }
    }

    public void setDepartment_id(String department_id)
    {
        if(department_id != null)
        {
            this.department_id = department_id;
        }
    }

    public void setSection_id(String section_id)
    {
        if(section_id != null)
        {
            this.section_id = section_id;
        }
    }

    public void setCategory_id(String category_id)
    {
        if(category_id != null)
        {
            this.category_id = category_id;
        }
    }

    public void setSubCategory_id(String subcategory_id)
    {
        if(subcategory_id != null)
        {
            this.subcategory_id = subcategory_id;
        }
    }
    
    public void setMake(String make)
    {
        if(make != null)
        {
            this.make = make;
        }
    }

    public void setLocation(String location)
    {
        if(location != null)
        {
            this.location = location;
        }
    }

    public void setMaintained_by(String maintained_by)
    {
        if(maintained_by != null)
        {
            this.maintained_by = maintained_by;
        }
    }

    public void setDriver(String driver)
    {
        if(driver != null)
        {
            this.driver = driver;
        }
    }

    public void setState(String state)
    {
        if(state != null)
        {
            this.state = state;
        }
    }

    public void setSupplied_by(String supplied_by)
    {
        if(supplied_by != null)
        {
            this.supplied_by = supplied_by;
        }
    }

    public void setRegistration_no(String registration_no)
    {
        if(registration_no != null)
        {
            this.registration_no = registration_no;
        }
    }

    public void setDate_of_purchase(String date_of_purchase)
    {
        if(date_of_purchase != null)
        {
            this.date_of_purchase = DateManipulations.DateToCalendar(date_of_purchase);
        }
    }

    public void setDepreciation_start_date(String depreciation_start_date)
    {
        if(depreciation_start_date != null)
        {
            this.depreciation_start_date = DateManipulations.DateToCalendar(depreciation_start_date);
        }
    }

    public void setDepreciation_end_date(String depreciation_end_date)
    {
        if(depreciation_end_date != null)
        {
            this.depreciation_end_date = DateManipulations.DateToCalendar(depreciation_end_date);
        }
    }

    public void setAuthorized_by(String authorized_by)
    {
        if(authorized_by != null)
        {
            this.authorized_by = authorized_by;
        }
    }

    public void setReason(String reason)
    {
        if(reason != null)
        {
            this.reason = reason;
        }
    }

    public void setNo_of_items(String no_of_items)
    {
        if(no_of_items != null)
        {
            this.no_of_items = no_of_items.replaceAll(",", "");
        }
    }

    public void setDescription(String description)
    {
        if(description != null)
        {
            this.description = description;
        }
    }

    public void setCost_price(String cost_price)
    {
        if(cost_price != null)
        {
            this.cost_price = cost_price.replaceAll(",", "");
        }
    }

    public void setVatable_cost(String vatable_cost)
    {
        if(vatable_cost != null)
        {
            this.vatable_cost = vatable_cost.replaceAll(",", "");
        }
    }

    public void setSubject_to_vat(String subject_to_vat)
    {
        if(subject_to_vat != null)
        {
            this.subject_to_vat = subject_to_vat.replaceAll(",", "");
        }
    }

    public void setVat_amount(String vat_amount)
    {
        if(vat_amount != null)
        {
            this.vat_amount = vat_amount.replaceAll(",", "");
        }
    }

    public void setWh_tax_cb(String wh_tax_cb)
    {
        if(wh_tax_cb != null)
        {
            this.wh_tax_cb = wh_tax_cb;
        }
    }

    public void setWh_tax_amount(String wh_tax_amount)
    {
        if(wh_tax_amount != null)
        {
            this.wh_tax_amount = wh_tax_amount.replaceAll(",", "");
        }
    }

    public void setSerial_number(String serial_number)
    {
        if(serial_number != null)
        {
            this.serial_number = serial_number;
        }
    }

    public void setEngine_number(String engine_number)
    {
        if(engine_number != null)
        {
            this.engine_number = engine_number;
        }
    }

    public void setModel(String model)
    {
        if(model != null)
        {
            this.model = model;
        }
    }

    public void setUser(String user)
    {
        if(user != null)
        {
            this.user = user;
        }
    }

    public void setDepreciation_rate(String depreciation_rate)
    {
        if(depreciation_rate != null)
        {
            this.depreciation_rate = depreciation_rate.replaceAll(",", "");
        }
    }

    public void setResidual_value(String residual_value)
    {
        if(residual_value != null)
        {
            this.residual_value = residual_value.replaceAll(",", "");
        }
    }

    public void setRequire_depreciation(String require_depreciation)
    {
        if(require_depreciation != null)
        {
            this.require_depreciation = require_depreciation;
        }
    }

    public void setVendor_account(String vendor_account)
    {
        if(vendor_account != null)
        {
            this.vendor_account = vendor_account;
        }
    }

    public void setSpare_1(String spare_1)
    {
        if(spare_1 != null)
        {
            this.spare_1 = spare_1;
        }
    }

    public void setSpare_2(String spare_2)
    {
        if(spare_2 != null)
        {
            this.spare_2 = spare_2;
        }
    }

    public void setSpare_3(String spare_3)
    {
        if(spare_3 != null)
        {
            this.spare_3 = spare_3;
        }
    }

    public void setSpare_4(String spare_4)
    {
        if(spare_4 != null)
        {
            this.spare_4 = spare_4;
        }
    }

    public void setSpare_5(String spare_5)
    {
        if(spare_5 != null)
        {
            this.spare_5 = spare_5;
        }
    }

    public void setSpare_6(String spare_6)
    {
        if(spare_6 != null)
        {
            this.spare_6 = spare_6;
        }
    }

    public void setWho_to_rem(String who_to_rem)
    {
        if(who_to_rem != null)
        {
            this.who_to_rem = who_to_rem;
        }
    }

    public void setEmail_1(String email_1)
    {
        if(email_1 != null)
        {
            this.email_1 = email_1;
        }
    }

    public void setWho_to_rem_2(String who_to_rem_2)
    {
        if(who_to_rem_2 != null)
        {
            this.who_to_rem_2 = who_to_rem_2;
        }
    }

    public void setEmail2(String email2)
    {
        if(email2 != null)
        {
            this.email2 = email2;
        }
    }

    public void setUser_id(String user_id)
    {
        if(user_id != null)
        {
            this.user_id = user_id;
        }
    }

    public void setPosting_date(String s)
    {
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public void setWarrantyStartDate(String warDate)
    {
        if(warrantyStartDate != null)
        {
            warrantyStartDate = DateManipulations.DateToCalendar(warDate);
        }
    }

    public void setNoOfMonths(String months)
    {
        if(noOfMonths != null)
        {
            noOfMonths = months;
        }
    }

    public void setExpiryDate(String expiryDate)
    {
        if(expiryDate != null)
        {
            this.expiryDate = DateManipulations.DateToCalendar(expiryDate);
        }
    }

    public String getBranch_id()
    {
        return branch_id;
    }

    public String getDepartment_id()
    {
        return department_id;
    }

    public String getSection_id()
    {
        return section_id;
    }

    public String getCategory_id()
    {
        return category_id;
    }

    public String getSubCategory_id()
    {
        return subcategory_id;
    }
    
    public String getMake()
    {
        return make;
    }

    public String getLocation()
    {
        return location;
    }

    public String getMaintained_by()
    {
        return maintained_by;
    }

    public String getDriver()
    {
        return driver;
    }

    public String getState()
    {
        return state;
    }

    public String getSupplied_by()
    {
        return supplied_by;
    }

    public String getRegistration_no()
    {
        return registration_no;
    }

    public String getDate_of_purchase()
    {
        return DateManipulations.CalendarToDate(date_of_purchase);
    }

    public String getDepreciation_start_date()
    {
        return DateManipulations.CalendarToDate(depreciation_start_date);
    }

    public String getDepreciation_end_date()
    {
        return DateManipulations.CalendarToDate(depreciation_end_date);
    }

    public String getAuthorized_by()
    {
        return authorized_by;
    }

    public String getReason()
    {
        return reason;
    }

    public String getNo_of_items()
    {
        return no_of_items;
    }

    public String getDescription()
    {
        return description;
    }

    public String getCost_price()
    {
        return cost_price;
    }

    public String getVatable_cost()
    {
        return vatable_cost;
    }

    public String getSubject_to_vat()
    {
        return subject_to_vat;
    }

    public String getVat_amount()
    {
        return vat_amount;
    }

    public String getWh_tax_cb()
    {
        return wh_tax_cb;
    }

    public String getWh_tax_amount()
    {
        return wh_tax_amount;
    }

    public String getSerial_number()
    {
        return serial_number;
    }

    public String getEngine_number()
    {
        return engine_number;
    }

    public String getModel()
    {
        return model;
    }

    public String getUser()
    {
        return user;
    }

    public String getDepreciation_rate()
    {
        return depreciation_rate;
    }

    public String getResidual_value()
    {
        return residual_value;
    }

    public String getRequire_depreciation()
    {
        return require_depreciation;
    }

    public String getVendor_account()
    {
        return vendor_account;
    }

    public String getSpare_1()
    {
        return spare_1;
    }

    public String getSpare_2()
    {
        return spare_2;
    }

    public String getSpare_3()
    {
        return spare_3;
    }

    public String getSpare_4()
    {
        return spare_4;
    }

    public String getSpare_5()
    {
        return spare_5;
    }

    public String getSpare_6()
    {
        return spare_6;
    }

    public String getWho_to_rem()
    {
        return who_to_rem;
    }

    public String getEmail_1()
    {
        return email_1;
    }

    public String getWho_to_rem_2()
    {
        return who_to_rem_2;
    }

    public String getEmail2()
    {
        return email2;
    }

    public String getUser_id()
    {
        return user_id;
    }

    public String getProvince()
    {
        return province;
    }

    public String getPosting_date()
    {
        return DateManipulations.CalendarToDate(posting_date);
    }

    public String getWarrantyStartDate()
    {
        return DateManipulations.CalendarToDate(warrantyStartDate);
    }

    public String getNoOfMonths()
    {
        return noOfMonths;
    }

    public String getExpiryDate()
    {
        return DateManipulations.CalendarToDate(expiryDate);
    }
    
    public String getAssetuser()
    {
        return assetuser;
    }
    public void setAssetuser(String assetuser)
    {
        this.assetuser = assetuser;
    }
    
    public String getSerialNo()
    {
        return serialNo;
    }
    public void setSerialNo(String serialNo)
    {
        this.serialNo = serialNo;
    }
    
    public String getVendor()
    {
        return vendor;
    }
    public void setVendor(String vendor)
    {
        this.vendor = vendor;
    }
    
    public String getAssetMaintainBy()
    {
        return assetMaintainBy;
    }
    public void setAssetMaintainBy(String assetMaintainBy)
    {
        this.assetMaintainBy = assetMaintainBy;
    }
    
    public String getPurchaseReason()
    {
        return purchaseReason;
    }
    public void setPurchaseReason(String purchaseReason)
    {
        this.purchaseReason = purchaseReason;
    }    
    
    public String getProjectCode()
    {
        return projectCode;
    }
    public void setProjectCode(String projectCode)
    {
        this.projectCode = projectCode;
    }   
    
    public String getRegionCode()
    {
        return regionCode;
    }
    public void setRegionCode(String regionCode)
    {
        this.regionCode = regionCode;
    }  
    
    public String getZoneCode()
    {
        return zoneCode;
    }
    public void setZoneCode(String zoneCode)
    {
        this.zoneCode = zoneCode;
    }      
    
    public String getResidualvalue()
        throws Exception
    {
        String selectQuery;
        String residualValue;
        selectQuery = "SELECT RESIDUAL_VALUE FROM AM_GB_COMPANY";
        residualValue = "0.00";
        try
        {
            ResultSet rs = getStatement().executeQuery(selectQuery);
            rs.next();
            residualValue = rs.getString(1);
        }
        catch(Exception e)
        {
            System.out.println((new StringBuilder("INFO: Error getting residualValue >>")).append(e).toString()); 
        }
        return residualValue;
    }

    public String getCreatedGroupId(String category, String description)
    {
        String id;
        String selectQuery;
        id = "";
        selectQuery = (new StringBuilder("SELECT ASSET_ID FROM AM_GROUP_ASSET_BID WHERE CATEGORY_ID = ")).append(category).append(" AND DESCRIPTION = '").append(description).append("'").toString();
        try
        {
            ResultSet rs = getStatement().executeQuery(selectQuery);
            rs.next();
            id = rs.getString(1);
            freeResource();
        }
        catch(Exception er)
        {
            System.out.println((new StringBuilder("INFO: Error fetching groupid >>")).append(er).toString());
            id = "";

        }
        return id;
    }

    public int insertGroupAssetRecord()
        throws Exception, Throwable
    {

        int DONE = 0;

        int value = 0;
        createGroup();
        value = DONE;

        return value;
    }
    public int insertGroupAssetRecordUpload()
    throws Exception, Throwable
{
    	section_id = "0";
        asset_id = new legend.AutoIDSetup().getBidIdentity(branch_id,
                department_id, section_id, category_id);
  //      String codeno = approve.getCodeName("select mt_id from IA_MTID_TABLE where mt_tablename = 'AM_ASSET'");
 //       String codeno =  new ApplicationHelper().getGeneratedId("AM_ASSET"); 
//        int newcode = Integer.parseInt(codeno);
        String gid = approve.getCodeName("select count(*) from AM_ASSET_BID");
        gid = "BI"+gid;
        //    	String gid =  new ApplicationHelper().getGeneratedId("AM_GROUP_ASSET_MAIN"); 
//        String asset_code = Integer.toString(newcode);
        String asset_code = "0";
//    System.out.println("=====insertGroupAssetRecordUpload asset_id=====> "+asset_id+"   gid: "+gid);
    int DONE = 0;

    int value = 0;

        createGroupUpload(asset_id,gid,asset_code);
        value = DONE;

    return value;
}


    public String[] getBudgetInfo()
    {
        String result[];
        String query;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        result = new String[5];
        query = " SELECT financial_start_date,financial_no_ofmonths,financial_end_date,enforce_ac" +
"q_budget,quarterly_surplus_cf FROM am_gb_company"
;
        con = null;
        ps = null;
        rs = null;
        try
        {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            for(rs = ps.executeQuery(); rs.next();)
            {
                result[0] = sdf.format(rs.getDate("financial_start_date"));
                result[1] = rs.getString("financial_no_ofmonths");
                result[2] = sdf.format(rs.getDate("financial_end_date"));
                result[3] = rs.getString("enforce_acq_budget");
                result[4] = rs.getString("quarterly_surplus_cf");
            }

        }
        catch(Exception e)
        {
            String warning = (new StringBuilder("WARNING:Error Fetching Company Details ->")).append(e.getMessage()).toString();
            System.out.println(warning);
        }

        dbConnection.closeConnection(con, ps, rs);

        dbConnection.closeConnection(con, ps, rs);
        dbConnection.closeConnection(con, ps, rs);
        return result;
    }


    public String getCatCode()
    {
        String query;
        String catid;
        query = (new StringBuilder("SELECT CATEGORY_CODE  FROM am_ad_category  WHERE category_id = '")).append(category_id).append("' ").toString();
        catid = "0";
        try
        {
            for(ResultSet rs = getStatement().executeQuery(query); rs.next();)
            {
                catid = rs.getString(1);
            }

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        return catid;
    }

    private String getDepreciationRate(String category_id)
        throws Exception
    {
        Connection con;
        PreparedStatement ps;
        String rate;
        String query;
        con = null;
        ps = null;
 //       ResultSet rs = null;
        rate = "0.0";
        query = (new StringBuilder("SELECT DEP_RATE FROM AM_AD_CATEGORY WHERE CATEGORY_ID = ")).append(category_id).toString();
        try
        {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            for(ResultSet rs = ps.executeQuery(); rs.next();)
            {
                rate = rs.getString(1);
            }

        }
        catch(Exception ex)
        {
            System.out.println((new StringBuilder("WARN: Error fetching DepreciationRate ->")).append(ex).toString());
        }

        dbConnection.closeConnection(con, ps);
        dbConnection.closeConnection(con, ps);

        dbConnection.closeConnection(con, ps);
        return rate;
    }

    public String computeTotalLife(String depRate)
    {
        String totalLife = "0";
        if(depRate == null || depRate.equals(""))
        {
            depRate = "0.0";
        }
        double division = 100D / Double.parseDouble(depRate);
        int intTotal = (int)(division * 12D);
        totalLife = Integer.toString(intTotal);
        return totalLife;
    }

    public String getRaise_entry()
    {
        return raise_entry;
    }

    public void setRaise_entry(String raise_entry)
    {
        this.raise_entry = raise_entry;
    }

    public String getAmountPTD()
    {
        return amountPTD;
    }

    public void setAmountPTD(String amountPTD)
    {
        this.amountPTD = amountPTD;
    }

    public String getAmountREM()
    {
        return amountREM;
    }

    public void setAmountREM(String amountREM)
    {
        this.amountREM = amountREM;
    }

    public String getFullyPAID()
    {
        return fullyPAID;
    }

    public void setFullyPAID(String fullyPAID)
    {
        this.fullyPAID = fullyPAID;
    }

    public void setExpiryDate(Calendar expiryDate)
    {
        this.expiryDate = expiryDate;
    }

    public void setWarrantyStartDate(Calendar warrantyStartDate)
    {
        this.warrantyStartDate = warrantyStartDate;
    }

    public String getPartPAY()
    {
        return partPAY;
    }

    public void setPartPAY(String partPAY)
    {
        this.partPAY = partPAY;
    }

    public String getGid()
    {
        return gid;
    }

    public void setGid(String gid)
    {
        this.gid = gid;
    }
    public void setSerial_no(String serial_no)
    {
        if(serial_no != null)
        {
            this.serial_no = serial_no;
        }
    }
    public void setInvoice_No(String invoice_No)
    {
        if(invoice_No != null)
        {
            this.invoice_No = invoice_No;
        }
    }
    public void setLpo_no(String lpo_no)
    {
        if(lpo_no != null)
        {
            this.lpo_no = lpo_no;
        }
    }
    public void setBar_code(String bar_code)
    {
        if(bar_code != null)
        {
            this.bar_code = bar_code;
        }
    }
    public void setWh_tax(String wh_tax)
    {
        if(wh_tax != null)
        {
            this.wh_tax = wh_tax;
        }
    }
    public void setBranch_code(String branch_code)
    {
        if(branch_code != null)
        {
            this.branch_code = branch_code;
        }
    }
    
    public void setDepartment_code(String department_code)
    {
        if(department_code != null)
        {
            this.department_code = department_code;
        }
    }
    public void setSection_code(String section_code)
    {
        if(section_code != null)
        {
            this.section_code = section_code;
        }
    }
    public void setSbu_code(String sbu_code)
    {
        if(sbu_code != null)
        {
            this.sbu_code = sbu_code;
        }
    }   
    public void setLocationCode(String locationCode)
    {
        if(locationCode != null)
        {
            this.locationCode = locationCode;
        }
    }
        
    public void setCategory_code(String category_code)
    {
        if(category_code != null)
        {
            this.category_code = category_code;
        }
    }
    public void setSubCategory_code(String subcategory_code)
    {
        if(subcategory_code != null)
        {
            this.subcategory_code = subcategory_code;
        }
    }

    
}
