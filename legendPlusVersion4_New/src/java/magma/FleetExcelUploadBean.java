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

public class FleetExcelUploadBean extends ConnectionClass
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
    private Calendar first_date_obtained;
    private Calendar next_date_obtained;
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
    private String tranType;
    private String improveReason;
    private String projectCode;
    private String regionCode;
    private String zoneCode;
    private String vendorId;
    private String vendorAcct;
    private String glAccount;

    public FleetExcelUploadBean()
        throws Exception
    {
        branch_id = "0";
        department_id = "0";
        section_id = "0";
        category_id = "0";
        subcategory_id = "0";
        location = "0";
        registration_no = "";
        first_date_obtained = new GregorianCalendar();
        next_date_obtained = new GregorianCalendar();
        depreciation_end_date = new GregorianCalendar();
        description = "";
        cost_price = "0";
        user_id = "0";
        posting_date = new GregorianCalendar();
        gid = "1";
        expiryDate = new GregorianCalendar();
        sdf = new SimpleDateFormat("dd-MM-yyyy");
        dbConnection = new MagmaDBConnection();
        dateFormat = new DatetimeFormat();
        processflag ='N';
        postflag = 'P';
        bar_code = "";
        wh_tax = "";
        branch_code = "";
        department_code = "";
        category_code = "";
        section_code = "";
        sbu_code = "";
        assetuser = "";
        approve = new ApprovalRecords(); 
        htmlUtil = new HtmlUtility(); 
        projectCode = "";
        regionCode = "";
    	zoneCode = "";
    	asset_code = "";
    	vendorId = "";
    	vendorAcct = "";
    	glAccount = "";
//    	transType = "0";

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
        String approvalStatus = "PENDING";
        String improved = "Y";
        double  costPrice = Double.parseDouble(cost_price)+Double.parseDouble(wh_tax_amount)/100;
        String query = "INSERT INTO FT_GROUP_DUE_PERIOD(GROUP_ID,TRANS_TYPE,BRANCH,BRANCH_ID,ASSET_ID,BAR_CODE,REGISTRATION_NO,LICENCE_NO,FIRST_DATE_OBTAINED," +
"NEXT_DATE_OBTAINED,COST_PRICE,POSTING_DATE,USER_ID,DESCRIPTION,SBU_CODE,VENDOR_ID,VENDOR_ACCT,GLACCOUNT,STATUS,category_id,CATEGORY_CODE" +
" )\tVALUES('";
        
        b.append(query);
        b.append(gid);
        b.append("',");
        b.append(tranType);
        b.append("',");
        b.append(branch_code);
        b.append("',");
        b.append(branch_id);        
        b.append("',");
        b.append(Assetid);        
        b.append("',");
        b.append(bar_code);                
        b.append(",");
        b.append(registration_no);
        b.append(",");
        b.append(registration_no);
        b.append(",");
        b.append(DateManipulations.CalendarToDb(first_date_obtained));
        b.append(",");
        b.append(DateManipulations.CalendarToDb(next_date_obtained));        
        b.append(",");
        b.append(costPrice);          
        b.append(",");
        b.append(DateManipulations.CalendarToDb(posting_date));             
        b.append(",'");
        b.append(user_id);
        b.append(",'");
        b.append(description);        
        b.append("','");
        b.append(sbu_code);        
        b.append("','");
        b.append(vendorId);        
        b.append("','");
        b.append(vendorAcct);     
        b.append("','");
        b.append(glAccount);  
        b.append("','");
        b.append("N");
        b.append("','");
        b.append(category_id);    
        b.append("','");
        b.append(category_code);       
        b.append("')");
        try
        {   
            getStatement().executeUpdate(b.toString());
        }
        catch(Exception r)
        {
            System.out.println((new StringBuilder("INFO: Error creating group Fleet Upload >>")).append(r).toString());
        }
    }

public void createGroupUpload(String gid,String asset_code)
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
/*if(transType == null || transType.equals(""))
{
	transType = "0";
}
*/
if(province == null || province.equals(""))
{
    province = "0";
}

String approvalStatus = "PENDING";
String improved = "Y";
double  costPrice = Double.parseDouble(cost_price);
String query = "INSERT INTO FT_GROUP_DUE_PERIOD(GROUP_ID,TRANS_TYPE,BRANCH,BRANCH_ID,ASSET_ID,BAR_CODE,REGISTRATION_NO,LICENCE_NO,FIRST_DATE_OBTAINED," +
"NEXT_DATE_OBTAINED,COST_PRICE,POSTING_DATE,USER_ID,DESCRIPTION,SBU_CODE,VENDOR_ID,VENDOR_ACCT,GLACCOUNT,STATUS," +
"CATEGORY_ID,CATEGORY_CODE )VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

try {   
	con = getConnection();  
	ps = con.prepareStatement(query);
	ps.setString(1, gid); 
	ps.setString(2, tranType);
	ps.setString(3, branch_code);
	ps.setString(4, branch_id);
	ps.setString(5, Assetid);
	ps.setString(6, bar_code);
	ps.setString(7, registration_no);
	ps.setString(8, registration_no);
	ps.setString(9, DateManipulations.CalendarToDb(first_date_obtained));
	ps.setString(10, DateManipulations.CalendarToDb(next_date_obtained));
	ps.setDouble(11, costPrice);
	ps.setString(12, DateManipulations.CalendarToDb(posting_date));
	ps.setInt(13, Integer.parseInt(user_id));
	ps.setString(14, description);
	ps.setString(15, sbu_code);
	ps.setString(16, vendorId);
	ps.setString(17, vendorAcct);
	ps.setString(18, glAccount);
	ps.setString(19, "N");
	ps.setInt(20, Integer.parseInt(category_id));
	ps.setString(21, category_code);
	System.out.println("<<<<<<<createGroupUpload vendorId: "+vendorId+"   vendor_account: "+vendor_account+"   glAccount: "+glAccount);
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
	+ " INFO: Error creating group Fleet Upload>> "
		+ e.getMessage());
} finally {
	freeResource(con, ps);
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

    public void setFirst_date_obtained(String first_date_obtained)
    {
        if(first_date_obtained != null)
        {
            this.first_date_obtained = DateManipulations.DateToCalendar(first_date_obtained);
        }
    }

    public void setNext_date_obtained(String next_date_obtained)
    {
        if(next_date_obtained != null)
        {
            this.next_date_obtained = DateManipulations.DateToCalendar(next_date_obtained);
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

    public String getFirst_date_obtained()
    {
        return DateManipulations.CalendarToDate(first_date_obtained);
    }

    public String getNext_date_obtained()
    {
        return DateManipulations.CalendarToDate(next_date_obtained);
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
    
    public String getTranType()
    {
        return tranType;
    }
    public void setTranType(String tranType)
    {
        this.tranType = tranType;
    }
    
    public String getImproveReason()
    {
        return improveReason;
    }
    public void setImproveReason(String improveReason)
    {
        this.improveReason = improveReason;
    }    
    
    public String getProjectCode()
    {
        return projectCode;
    }
    public void setProjectCode(String projectCode)
    {
        this.projectCode = projectCode;
    }   
    
    public String getAsset_code()
    {
        return asset_code;
    }
    public void setAsset_code(String asset_code)
    {
        this.asset_code = asset_code;
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
    
    public String getAsset_id()
    {
        return asset_id;
    }
    public void setAsset_id(String asset_id)
    {
        this.asset_id = asset_id;
    } 
    
    public String getVendorId()
    {
        return vendorId;
    }
    public void setVendorId(String vendorId)
    {
        this.vendorId = vendorId;
    } 
    
    public String getVendorAcct()
    {
        return vendorAcct;
    }
    public void setVendorAcct(String vendorAcct)
    {
        this.vendorAcct = vendorAcct;
    }    
    
    public String getGlAccount()
    {
        return glAccount;
    }
    public void setGlAccount(String glAccount)
    {
        this.glAccount = glAccount;
    }        
    
/*    
    public String getTransType()
    {
        return transType;
    }
    public void setTransType(String transType)
    {
        this.transType = transType;
    }  */ 
    public int insertGroupAssetImproveRecord()
        throws Exception, Throwable
    {
        String budget[] = getBudgetInfo();
        double bugdetvalues[] = getBudgetValues();
        int DONE = 0;
        int BUDGETENFORCED = 1;
        int BUDGETENFORCEDCF = 2;
        int ASSETPURCHASEBD = 3;
        int value = 0;
        String Q = getQuarter();
        if(budget[3].equalsIgnoreCase("N"))
        {
            createGroup();
            value = DONE;
        } else
        if(budget[3].equalsIgnoreCase("Y"))
        {
            if(!Q.equalsIgnoreCase("NQ"))
            {
                if(budget[3].equalsIgnoreCase("Y") && budget[4].equalsIgnoreCase("N"))
                {
                    if(chkBudgetAllocation(Q, bugdetvalues, false))
                    {
                        updateBudget(Q, budget);
                        createGroup();
                        value = DONE;
                    } else
                    {
                        value = BUDGETENFORCED;
                    }
                } else
                if(budget[3].equalsIgnoreCase("Y") && budget[4].equalsIgnoreCase("Y"))
                {
                    if(chkBudgetAllocation(Q, bugdetvalues, true))
                    {
                        updateBudget(Q, budget);
                        createGroup();
                        value = DONE;
                    } else
                    {
                        value = BUDGETENFORCEDCF;
                    }
                } else
                {
                    createGroup();
                    value = DONE;
                }
            } else
            {
                createGroup();
                value = ASSETPURCHASEBD;
            }
        }
        return value;
    }
    public int insertGroupFleetRecordUpload()
    throws Exception, Throwable
{
 //       asset_id = new legend.AutoIDSetup().getIdentity(branch_id,
 //               department_id, section_id, category_id);
  //      String codeno = approve.getCodeName("select mt_id from IA_MTID_TABLE where mt_tablename = 'AM_ASSET'");
//        String codeno =  new ApplicationHelper().getGeneratedId("AM_ASSET"); 
//        int newcode = Integer.parseInt(codeno);
//        String gid = approve.getCodeName("select mt_id from IA_MTID_TABLE where mt_tablename = 'am_group_asset_main'");
//    	String gid =  new ApplicationHelper().getGeneratedId("AM_GROUP_ASSET_MAIN"); 
    	 String gid = approve.getCodeName("select mt_id from IA_MTID_TABLE where mt_tablename = 'am_group_asset_main'");
//        String asset_code = Integer.toString(newcode);
    String budget[] = getBudgetInfo();
    double bugdetvalues[] = getBudgetValues();
    System.out.println("=====insertGroupFleetRecordUpload asset_id=====> "+asset_id+"   gid: "+gid);
    int DONE = 0;
    int BUDGETENFORCED = 1;
    int BUDGETENFORCEDCF = 2;
    int ASSETPURCHASEBD = 3;
    int value = 0;
    String Q = getQuarter();
    if(budget[3].equalsIgnoreCase("N"))
    {
        createGroupUpload(gid,asset_code);
        value = DONE;
    } else
    if(budget[3].equalsIgnoreCase("Y"))
    {
        if(!Q.equalsIgnoreCase("NQ"))
        {
            if(budget[3].equalsIgnoreCase("Y") && budget[4].equalsIgnoreCase("N"))
            {
                if(chkBudgetAllocation(Q, bugdetvalues, false))
                {
                    updateBudget(Q, budget);
                    createGroupUpload(gid,asset_code);
                    value = DONE;
                } else
                {
                    value = BUDGETENFORCED;
                }
            } else
            if(budget[3].equalsIgnoreCase("Y") && budget[4].equalsIgnoreCase("Y"))
            {
                if(chkBudgetAllocation(Q, bugdetvalues, true))
                {
                    updateBudget(Q, budget);
                    createGroupUpload(gid,asset_code);
                    value = DONE;
                } else
                {
                    value = BUDGETENFORCEDCF;
                }
            } else
            {
                createGroup();
                value = DONE;
            }
        } else
        {
            createGroupUpload(gid,asset_code);
            value = ASSETPURCHASEBD;
        }
    }
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
        }finally {
        	dbConnection.closeConnection(con, ps, rs);
		}
        return result;
    }

    public double[] getBudgetValues()
    {
        double result[];
        String query;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        result = new double[8];
        query = (new StringBuilder(" SELECT [Q1_ALLOCATION],[Q1_ACTUAL],[Q2_ALLOCATION],[Q2_ACTUAL],[Q3_ALLOCATION]," +
"[Q3_ACTUAL],[Q4_ALLOCATION],[Q4_ACTUAL] FROM [AM_ACQUISITION_BUDGET] WHERE [CATE" +
"GORY]='"
)).append(getCatCode()).append("' AND ").append(" [BRANCH_ID]='").append(branch_id).append("'").toString();
        con = null;
        ps = null;
        rs = null;
        try
        {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            for(rs = ps.executeQuery(); rs.next();)
            {
                result[0] = rs.getDouble("Q1_ALLOCATION");
                result[1] = rs.getDouble("Q1_ACTUAL");
                result[2] = rs.getDouble("Q2_ALLOCATION");
                result[3] = rs.getDouble("Q2_ACTUAL");
                result[4] = rs.getDouble("Q3_ALLOCATION");
                result[5] = rs.getDouble("Q3_ACTUAL");
                result[6] = rs.getDouble("Q4_ALLOCATION");
                result[7] = rs.getDouble("Q4_ACTUAL");
            }

        }
        catch(Exception e)
        {
            String warning = (new StringBuilder("WARNING:Error Fetching Company Details ->")).append(e.getMessage()).toString();
            System.out.println(warning);
        }finally {
        	dbConnection.closeConnection(con, ps, rs);
		}
        return result;
    }

    public String getQuarter()
    {
        String quarter = "NQ";
        String budg[] = getBudgetInfo();
        double q1 = Double.parseDouble(budg[1]) / 4D;
        int month = dateFormat.getDayDifference(sdf.format(first_date_obtained.getTime()), budg[0]) / 30;
        boolean btw = dateFormat.isDateBetween(budg[0], budg[2], sdf.format(first_date_obtained.getTime()));
        if(btw)
        {
            if((double)month <= q1)
            {
                quarter = "FIRST";
                System.out.println("1st Quarter");
            } else
            if((double)month > q1 && (double)month <= q1 * 2D)
            {
                quarter = "2ND";
                System.out.println("2nd Quarter");
            } else
            if((double)month > q1 * 2D && (double)month <= q1 * 3D)
            {
                quarter = "3RD";
                System.out.println("3rd Quarter");
            } else
            if((double)month > q1 * 3D)
            {
                quarter = "4TH";
                System.out.println("4th Quarter");
            }
        }
        return quarter;
    }

    public String getCatCode()
    {
        String query;
        String catid;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        query = (new StringBuilder("SELECT CATEGORY_CODE  FROM am_ad_category  WHERE category_id = '")).append(category_id).append("' ").toString();
        catid = "0";

        try
        {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
            for(rs = ps.executeQuery(); rs.next();)
            {
                catid = rs.getString(1);
            }

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }finally {
        	dbConnection.closeConnection(con, ps, rs);
		}

        return catid;
    }

    public boolean chkBudgetAllocation(String quarter, double values[], boolean cf)
    {
        boolean allocation = true;
        double result = 0.0D;
        if(cf)
        {
            if(quarter.equalsIgnoreCase("FIRST"))
            {
                result = values[0] - (values[1] + Double.parseDouble(vatable_cost.replaceAll(",", "")));
            } else
            if(quarter.equalsIgnoreCase("2ND"))
            {
                result = (values[0] + values[2]) - (values[1] + values[3] + Double.parseDouble(vatable_cost.replaceAll(",", "")));
            } else
            if(quarter.equalsIgnoreCase("3RD"))
            {
                result = (values[0] + values[2] + values[4]) - (values[1] + values[3] + values[5] + Double.parseDouble(vatable_cost.replaceAll(",", "")));
            } else
            if(quarter.equalsIgnoreCase("4TH"))
            {
                result = (values[0] + values[2] + values[4] + values[6]) - (values[1] + values[3] + values[5] + values[7] + Double.parseDouble(vatable_cost.replaceAll(",", "")));
            }
        } else
        if(quarter.equalsIgnoreCase("FIRST"))
        {
            result = values[0] - (values[1] + Double.parseDouble(vatable_cost.replaceAll(",", "")));
        } else
        if(quarter.equalsIgnoreCase("2ND"))
        {
            result = values[2] - (values[3] + Double.parseDouble(vatable_cost.replaceAll(",", "")));
        } else
        if(quarter.equalsIgnoreCase("3RD"))
        {
            result = values[4] - (values[5] + Double.parseDouble(vatable_cost.replaceAll(",", "")));
        } else
        if(quarter.equalsIgnoreCase("4TH"))
        {
            result = values[6] - (values[7] + Double.parseDouble(vatable_cost.replaceAll(",", "")));
        }
        if(result < 0.0D)
        {
            allocation = false;
        }
        return allocation;
    }

    public void updateBudget(String quarter, String bugdetinfo[])
    {
        Connection conn;
        Statement stmt;
        String fisdate = "";
        int finomonth = 0;
        String fiedate = "";
        conn = dbConnection.getConnection("legendPlus");
        stmt = null;
        try
        {
            stmt = conn.createStatement();
            String old_category = getCatCode();
            if(quarter.equalsIgnoreCase("FIRST"))
            {
                String budgetUpdate1 = (new StringBuilder("UPDATE AM_ACQUISITION_BUDGET  SET Q1_ACTUAL = (Q1_ACTUAL + ")).append(vatable_cost.replaceAll(",", "")).append(") WHERE BRANCH_ID='").append(branch_id).append("' AND CATEGORY='").append(old_category).append("' AND ACC_START_DATE='").append(dateFormat.dateConvert(bugdetinfo[0])).append("' AND ACC_END_DATE='").append(dateFormat.dateConvert(bugdetinfo[2])).append("'").toString();
                stmt.executeUpdate(budgetUpdate1);
                System.out.println("Updated 1st Quarter");
            } else
            if(quarter.equalsIgnoreCase("2ND"))
            {
                String budgetUpdate1 = (new StringBuilder("UPDATE AM_ACQUISITION_BUDGET  SET Q2_ACTUAL = (Q2_ACTUAL + ")).append(vatable_cost.replaceAll(",", "")).append(") WHERE BRANCH_ID='").append(branch_id).append("' AND CATEGORY='").append(old_category).append("' AND ACC_START_DATE='").append(dateFormat.dateConvert(bugdetinfo[0])).append("' AND ACC_END_DATE='").append(dateFormat.dateConvert(bugdetinfo[2])).append("'").toString();
                stmt.executeUpdate(budgetUpdate1);
            } else
            if(quarter.equalsIgnoreCase("3RD"))
            {
                String budgetUpdate1 = (new StringBuilder("UPDATE AM_ACQUISITION_BUDGET  SET Q3_ACTUAL =(Q3_ACTUAL + ")).append(vatable_cost.replaceAll(",", "")).append(") WHERE BRANCH_ID='").append(branch_id).append("' AND CATEGORY='").append(old_category).append("' AND ACC_START_DATE='").append(dateFormat.dateConvert(bugdetinfo[0])).append("' AND ACC_END_DATE='").append(dateFormat.dateConvert(bugdetinfo[2])).append("'").toString();
                stmt.executeUpdate(budgetUpdate1);
            } else
            if(quarter.equalsIgnoreCase("4TH"))
            {
                String budgetUpdate1 = (new StringBuilder("UPDATE AM_ACQUISITION_BUDGET  SET Q4_ACTUAL = (Q4_ACTUAL + ")).append(vatable_cost.replaceAll(",", "")).append(") WHERE BRANCH_ID='").append(branch_id).append("' AND CATEGORY='").append(old_category).append("' AND ACC_START_DATE='").append(dateFormat.dateConvert(bugdetinfo[0])).append("' AND ACC_END_DATE='").append(dateFormat.dateConvert(bugdetinfo[2])).append("'").toString();
                stmt.executeUpdate(budgetUpdate1);
            }
        }
        catch(Exception ex)
        {
            System.out.println((new StringBuilder("ERROR_ ")).append(getClass().getName()).append("---").append(ex.getMessage()).append("--").toString());
            ex.printStackTrace();

        }finally {
        	dbConnection.closeConnection(conn, stmt);
		}
//        System.out.println("Exiting update of Aquicisition Budget due to Reclassification");
        return;
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
        }finally {
        	dbConnection.closeConnection(con, ps);
		}
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
    public void createAssetUpload()
    		throws Exception
    		{
    	System.out.print("======>asset_id: "+asset_id+"   Assetid: "+Assetid+"     ====cost_price: "+cost_price);
    		//Assetid = asset_id;
    			Connection con = null;
    			PreparedStatement ps = null;
    			boolean done = false;
    			String id = "";
    			double  costPrice = Double.parseDouble(cost_price);
    		String query = "INSERT INTO FLEET_SUMINSURED(ASSET_ID,REGISTRATION_NO,SUM_INSURED,EFFECTIVE_DATE,TRAN_TYPE"+
    			")VALUES (?,?,?,?,?)";
    		
    		try {   
    			con = getConnection();  
    			ps = con.prepareStatement(query);
    			ps.setString(1, asset_id); 
    			ps.setString(2, registration_no);
    			ps.setDouble(3, costPrice);
    			ps.setString(4, DateManipulations.CalendarToDb(first_date_obtained));
    			ps.setString(5, tranType);
    			done = (ps.executeUpdate() != -1);
//    			System.out.println("<<<<<<<createUserUpload done: "+done);
    			freeResource(con, ps);
    		} catch (Exception e) {

    		e.printStackTrace();
    		System.out.println(this.getClass().getName()
    			+ " INFO: Error in createUserUpload creating User Upload>> "
    				+ e.getMessage());
    		}finally {
            	dbConnection.closeConnection(con, ps);
    		}
    		}
    
}
