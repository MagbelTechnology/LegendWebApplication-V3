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

public class SbuExcelUploadBean extends ConnectionClass
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

    public SbuExcelUploadBean()
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
 //       Codes code = new Codes();
//        String approvalStatus = "PENDING";
 //       String improved = "Y";
 //       double  costPrice = Double.parseDouble(cost_price)+Double.parseDouble(wh_tax_amount)/100;
        String query = "INSERT INTO SBU_SETUP(SBU_CODE,SBU_NAME,SBU_CONTACT,CONTACT_EMAIL,STATUS)\tVALUES('";
        
        b.append(query);
        b.append("',");
        b.append(sbu_code);                 
        b.append(",");
        b.append(description);   
        b.append(",");
        b.append(registration_no);
        b.append(",");
        b.append(bar_code); 
        b.append(",");
        b.append(tranType);
        b.append("')");
        try
        {   
            getStatement().executeUpdate(b.toString());
        }
        catch(Exception r)
        {
            System.out.println((new StringBuilder("INFO: Error creating group SBU Upload >>")).append(r).toString());
        }
    }

public void DeleteExistingSBU()
throws Exception
{
	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	String id = "";

String query2 = "delete from Sbu_SetUp where sbu_code in (select sbu_code from Sbu_SetUp_Upload)";
String query  = "INSERT INTO Sbu_SetUp_Upload(SBU_CODE,SBU_NAME,SBU_CONTACT,CONTACT_EMAIL,STATUS)VALUES(?,?,?,?,?)";

try {   
	con = getConnection();  
	ps = con.prepareStatement(query);
	ps.setString(1, sbu_code);
	ps.setString(2, description);
	ps.setString(3, registration_no);
	ps.setString(4, bar_code);
	ps.setString(5, tranType);
	System.out.println("<<<<<<<createSBUUpload sbu_code: "+sbu_code+"   description: "+description+"   registration_no: "+registration_no+"  bar_code: "+bar_code+"   tranType: "+tranType);
	done = (ps.executeUpdate() != -1);
	freeResource(con, ps);
	con = getConnection();  
	ps = con.prepareStatement(query2);
	done = (ps.executeUpdate() != -1);

} catch (Exception e) {

e.printStackTrace();
System.out.println(this.getClass().getName()
	+ " INFO: Error in DeleteExistingSBU creating SBU Upload>> "
		+ e.getMessage());
} finally {
	freeResource(con, ps);
}

}


public void createSBUUpload()
throws Exception
{
	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	String id = "";

String query = "INSERT INTO SBU_SETUP(SBU_CODE,SBU_NAME,SBU_CONTACT,CONTACT_EMAIL,STATUS)VALUES(?,?,?,?,?)";

try {   
	con = getConnection();  
	ps = con.prepareStatement(query);
	ps.setString(1, sbu_code);
	ps.setString(2, description);
	ps.setString(3, registration_no);
	ps.setString(4, bar_code);
	ps.setString(5, tranType);
	System.out.println("<<<<<<<createSBUUpload sbu_code: "+sbu_code+"   description: "+description+"   registration_no: "+registration_no+"  bar_code: "+bar_code+"   tranType: "+tranType);
	done = (ps.executeUpdate() != -1);

} catch (Exception e) {

e.printStackTrace();
System.out.println(this.getClass().getName()
	+ " INFO: Error in createSBUUpload creating SBU Upload>> "
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

    public int insertSBURecordUpload()  
    throws Exception, Throwable
{

    int DONE = 0;

    int value = 0;
    	DeleteExistingSBU();
       // createSBUUpload();
        value = DONE;
 
    return value;
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
    public String getBar_code() {
		return bar_code;
	}

	public String getSbu_code() {
		return sbu_code;
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
    
}
