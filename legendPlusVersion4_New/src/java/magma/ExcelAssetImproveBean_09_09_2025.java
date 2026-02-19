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

public class ExcelAssetImproveBean_09_09_2025 extends ConnectionClass
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
    private String improveReason;
    private String projectCode;
    private String regionCode;
    private String zoneCode;
    private String usefullife;
    
    private String oldcost_price;
    private String oldvatable_cost;
    private String oldaccum_dep;
    private String oldvat_amount;
    private String oldnbv;
    private String oldwht_amount;
    private String oldIMPROV_NBV;
    private String oldIMPROV_ACCUMDEP;
    private String oldIMPROV_COST;
    private String oldIMPROV_VATABLECOST;

    private String newcost_price;
    private String newvatable_cost;
    private String newvat_amount;
    private String newnbv;
    private String newwht_amount;
    public ExcelAssetImproveBean_09_09_2025()
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
        require_depreciation = "N";
        vendor_account = "";
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
        assetuser = "";
        approve = new ApprovalRecords(); 
        htmlUtil = new HtmlUtility(); 
        improveReason = "";
        projectCode = "";
        regionCode = "";
    	zoneCode = "";
    	asset_code = "";
    	usefullife = "";
       oldcost_price ="0";
       oldvatable_cost = "0";
       oldaccum_dep = "0";
       oldvat_amount = "0";
       oldnbv = "0";
       oldwht_amount = "0";
       oldIMPROV_NBV = "0";
       oldIMPROV_ACCUMDEP = "0";
       oldIMPROV_COST = "0";
       oldIMPROV_VATABLECOST = "0";
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
        String query = "INSERT INTO AM_GROUP_IMPROVEMENT(Revalue_ID,asset_id,cost_increase,revalue_reason,revalue_Date," +
"User_ID,raise_entry,r_vendor_ac,cost_price,vatable_cost," +
"vat_amount,wht_amount,nbv,accum_dep,old_cost_price,old_vatable_cost,old_vat_amount,old_wht_amount,old_nbv,old_accum_dep," +
"effDate,approval_status,WHT_PERCENT,WH_tax,Subject_to_vat,new_cost_price,new_vatable_cost,new_nbv,new_vat_amount," +
"new_wht_amount,R_VENDOR_ID,branch_code,category_code,description,lpoNum,invoice_no," +
"asset_code,IMPROV_USEFULLIFE,OLD_IMPROV_NBV,OLDIMPROV_ACCUMDEP,OLD_IMPROV_COST,OLD_IMPROV_VATABLECOST," +
"IMPROVED,PROJECT_CODE,LOCATION" +
" )\tVALUES('";
        
        b.append(query);
        b.append(gid);
        b.append("',");
        b.append(Assetid);
        b.append(",");
        b.append(costPrice);
        b.append(",");
        b.append(improveReason);
        b.append(",");
        b.append(DateManipulations.CalendarToDb(date_of_purchase));
        b.append(",'");
        b.append(user_id);
        b.append("','");
        b.append("N");
        b.append("','");
        b.append(vendor_account);
        b.append(",'");
        b.append(costPrice);
        b.append("',");
        b.append(Double.parseDouble(cost_price));
        b.append(",");
        b.append(Double.parseDouble(vat_amount));
        b.append(",'");
        b.append(Double.parseDouble(wh_tax_amount));
        b.append("',");
        b.append(0);
        b.append(",'");
        b.append(0);
        b.append("','");
        b.append(Double.parseDouble(oldcost_price));
        b.append("',");
        b.append(Double.parseDouble(oldvatable_cost));
        b.append(",'");
        b.append(Double.parseDouble(oldvat_amount));
        b.append("','");
        b.append(Double.parseDouble(oldwht_amount));
        b.append("',");
        b.append(Double.parseDouble(oldnbv));
        b.append(",");
        b.append(Double.parseDouble(oldnbv));
        b.append(",");
        b.append(Double.parseDouble(vatable_cost));
        b.append(",");
        b.append(Double.parseDouble(oldaccum_dep));
        b.append(",'");
        b.append(DateManipulations.CalendarToDb(warrantyStartDate));//effective Date
        b.append(",'");
        b.append(approvalStatus);
        b.append(",'");
        b.append(Double.parseDouble(wh_tax_amount));
        b.append(",'");
        b.append(wh_tax);
        b.append("','");
        b.append(subject_to_vat);
        b.append("','");
        b.append(Double.parseDouble(oldcost_price)+costPrice);
        b.append("','");
        b.append(Double.parseDouble(oldvatable_cost)+Double.parseDouble(vat_amount));
        b.append("','");
        b.append(Double.parseDouble(oldnbv)+costPrice);
        b.append("','");
        b.append(Double.parseDouble(oldvat_amount)+Double.parseDouble(vat_amount));
        b.append("','");
        b.append(Double.parseDouble(oldwht_amount)+Double.parseDouble(wh_tax_amount));
        b.append("',");
        b.append(supplied_by);
        b.append(",");
        b.append(branch_code);
        b.append(",");
        b.append(category_code);
        b.append(",");
        b.append(description);
        b.append(",");
        b.append(lpo_no);
        b.append(",");
        b.append(invoice_No);
        b.append(",");
        b.append(asset_code);
        b.append(",");
        b.append(Integer.parseInt(usefullife));
        b.append(",");
        b.append(Double.parseDouble(oldIMPROV_NBV));
        b.append(",");
        b.append(Double.parseDouble(oldIMPROV_ACCUMDEP));
        b.append(",");
        b.append(Double.parseDouble(oldIMPROV_COST));
        b.append(",");
        b.append(improved);
        b.append(",");
        b.append(projectCode);
        b.append("','");
        b.append(location);
       
 /*       b.append("','");
        b.append(improveReason);*/
        b.append("')");
        try
        {   
            getStatement().executeUpdate(b.toString());

        	if((getStatement() != null) || (!getStatement().equals(null))) {
			getStatement().close();}
        }
        catch(Exception r)
        {
            System.out.println((new StringBuilder("INFO: Error creating group aset Improvement >>")).append(r).toString());
        } 

    }

public void createGroupUpload(String asset_id, String gid,String asset_code)
throws Exception
{
	Connection con = null;
	PreparedStatement ps = null;
	PreparedStatement ps2 = null;
	boolean done = false;
	String id = "";
	
	Assetid = asset_id;
StringBuffer b;
b = new StringBuffer(400);
Codes code = new Codes();
double vatAmount = Double.parseDouble(vatable_cost)*(Double.parseDouble(vat_amount)/100);
double whtaxamount = Double.parseDouble(vatable_cost)*(Double.parseDouble(wh_tax_amount)/100);
if(no_of_items == null || no_of_items.equals(""))
{
    no_of_items = "0";
}
if(usefullife == null || usefullife.equals(""))
{
	usefullife = "0";
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
double  costPrice = Double.parseDouble(cost_price)+vatAmount;
//System.out.println("=====createGroupUpload vatAmount=====> "+vatAmount+"  whtaxamount: "+whtaxamount+"  costPrice: "+costPrice);
//System.out.println("=====createGroupUpload asset_id=====> "+asset_id);
//System.out.println("=====createGroupUpload description=====> "+description);
String query = "INSERT INTO AM_GROUP_IMPROVEMENT(Revalue_ID,asset_id,cost_increase,revalue_reason,revalue_Date," +
"User_ID,raise_entry,r_vendor_ac,cost_price,vatable_cost," +
"vat_amount,wht_amount,nbv,accum_dep,old_cost_price,old_vatable_cost,old_vat_amount,old_wht_amount,old_nbv,old_accum_dep," +
"effDate,approval_status,WHT_PERCENT,WH_tax,Subject_to_vat,new_cost_price,new_vatable_cost,new_nbv,new_vat_amount," +
"new_wht_amount,R_VENDOR_ID,branch_code,category_code,description,lpoNum,invoice_no," +
"asset_code,IMPROV_USEFULLIFE,OLD_IMPROV_NBV,OLDIMPROV_ACCUMDEP,OLD_IMPROV_COST,OLD_IMPROV_VATABLECOST," +
"IMPROVED,PROJECT_CODE,LOCATION"+
" )VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

String query2 = "INSERT INTO am_asset_improvement_Upload(Revalue_ID,asset_id,cost_increase,revalue_reason,revalue_Date," +
"User_ID,raise_entry,r_vendor_ac,cost_price,vatable_cost," +
"vat_amount,wht_amount,nbv,accum_dep,old_cost_price,old_vatable_cost,old_vat_amount,old_wht_amount,old_nbv,old_accum_dep," +
"effDate,approval_status,WHT_PERCENT,WH_tax,Subject_to_vat,new_cost_price,new_vatable_cost,new_nbv,new_vat_amount," +
"new_wht_amount,R_VENDOR_ID,branch_code,category_code,description,lpoNum,invoice_no," +
"asset_code,IMPROV_USEFULLIFE,OLD_IMPROV_NBV,OLDIMPROV_ACCUMDEP,OLD_IMPROV_COST,OLD_IMPROV_VATABLECOST," +
"IMPROVED,PROJECT_CODE,LOCATION"+
" )VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

try {   
	con = getConnection();  
	ps = con.prepareStatement(query);
	ps.setString(1, gid);
	ps.setString(2, Assetid);
	ps.setDouble(3, costPrice);
	ps.setString(4, improveReason);
	ps.setString(5, DateManipulations.CalendarToDb(date_of_purchase));
	ps.setInt(6, Integer.parseInt(user_id));
	ps.setString(7, "N");
	ps.setString(8, vendor_account);
	ps.setDouble(9, costPrice);
	ps.setDouble(10, Double.parseDouble(vatable_cost)); 
	ps.setDouble(11, vatAmount); 
	ps.setDouble(12, whtaxamount);
	ps.setDouble(13, 0); 
	ps.setDouble(14, 0); 
	ps.setDouble(15, Double.parseDouble(oldcost_price)); 
	ps.setDouble(16, Double.parseDouble(oldvatable_cost)); 
	ps.setDouble(17, Double.parseDouble(oldvat_amount)); 
	ps.setDouble(18, Double.parseDouble(oldwht_amount)); 
	ps.setDouble(19, Double.parseDouble(oldnbv)); 
	ps.setDouble(20, Double.parseDouble(oldaccum_dep)); 
	ps.setString(21, DateManipulations.CalendarToDb(posting_date));
	ps.setString(22, approvalStatus);
	ps.setDouble(23, Double.parseDouble(wh_tax_amount)); 
	ps.setString(24, wh_tax);
	ps.setString(25, subject_to_vat);
	ps.setDouble(26, Double.parseDouble(oldcost_price)+costPrice);
	ps.setDouble(27, Double.parseDouble(oldvatable_cost)+Double.parseDouble(cost_price));
	ps.setDouble(28, Double.parseDouble(oldnbv)+costPrice);
	ps.setDouble(29, Double.parseDouble(oldvat_amount)+vatAmount);
	ps.setDouble(30, Double.parseDouble(oldwht_amount)+whtaxamount);
	ps.setString(31, supplied_by);
	ps.setString(32, branch_code);
	ps.setString(33, category_code);
	ps.setString(34, description);
	ps.setString(35, lpo_no);
	ps.setString(36, invoice_No);
	ps.setString(37, asset_code);
	ps.setInt(38, Integer.parseInt(usefullife));
	ps.setDouble(39, Double.parseDouble(oldIMPROV_NBV));
	ps.setDouble(40, Double.parseDouble(oldIMPROV_ACCUMDEP));
	ps.setDouble(41, Double.parseDouble(oldIMPROV_COST));
	ps.setDouble(42, Double.parseDouble(oldIMPROV_VATABLECOST));
	ps.setString(43, improved);
	ps.setString(44, projectCode);
	ps.setString(45, location);
	
//	System.out.println("<<<<<<<createGroupUpload spare1: "+spare_1+"   spare2: "+spare_2+"   Group Id: "+gid);
done = (ps.executeUpdate() != -1);

ps2 = con.prepareStatement(query2);
ps2.setString(1, gid);
ps2.setString(2, Assetid);
ps2.setDouble(3, costPrice);
ps2.setString(4, improveReason);
ps2.setString(5, DateManipulations.CalendarToDb(date_of_purchase));
ps2.setInt(6, Integer.parseInt(user_id));
ps2.setString(7, "N");
ps2.setString(8, vendor_account);
ps2.setDouble(9, costPrice);
ps2.setDouble(10, Double.parseDouble(vatable_cost)); 
ps2.setDouble(11, vatAmount); 
ps2.setDouble(12, whtaxamount);
ps2.setDouble(13, 0); 
ps2.setDouble(14, 0); 
ps2.setDouble(15, Double.parseDouble(oldcost_price)); 
ps2.setDouble(16, Double.parseDouble(oldvatable_cost)); 
ps2.setDouble(17, Double.parseDouble(oldvat_amount)); 
ps2.setDouble(18, Double.parseDouble(oldwht_amount)); 
ps2.setDouble(19, Double.parseDouble(oldnbv)); 
ps2.setDouble(20, Double.parseDouble(oldaccum_dep)); 
ps2.setString(21, DateManipulations.CalendarToDb(posting_date));
ps2.setString(22, approvalStatus);
ps2.setDouble(23, Double.parseDouble(wh_tax_amount)); 
ps2.setString(24, wh_tax);
ps2.setString(25, subject_to_vat);
ps2.setDouble(26, Double.parseDouble(oldcost_price)+costPrice);
ps2.setDouble(27, Double.parseDouble(oldvatable_cost)+Double.parseDouble(cost_price));
ps2.setDouble(28, Double.parseDouble(oldnbv)+costPrice);
ps2.setDouble(29, Double.parseDouble(oldvat_amount)+vatAmount);
ps2.setDouble(30, Double.parseDouble(oldwht_amount)+whtaxamount);
ps2.setString(31, supplied_by);
ps2.setString(32, branch_code);
ps2.setString(33, category_code);
ps2.setString(34, description);
ps2.setString(35, lpo_no);
ps2.setString(36, invoice_No);
ps2.setString(37, asset_code);
ps2.setInt(38, Integer.parseInt(usefullife));
ps2.setDouble(39, Double.parseDouble(oldIMPROV_NBV));
ps2.setDouble(40, Double.parseDouble(oldIMPROV_ACCUMDEP));
ps2.setDouble(41, Double.parseDouble(oldIMPROV_COST));
ps2.setDouble(42, Double.parseDouble(oldIMPROV_VATABLECOST));
ps2.setString(43, improved);
ps2.setString(44, projectCode);
ps2.setString(45, location);

//System.out.println("<<<<<<<createGroupUpload spare1: "+spare_1+"   spare2: "+spare_2+"   Group Id: "+gid);
done = (ps2.executeUpdate() != -1);
//System.out.println("=====query=====> "+query);
if(done==true){
	id ="SUCCESS";
	htmlUtil.insToAm_Invoice_No(gid,lpo_no,invoice_No,"Asset Improve Upload");
}else{
	id ="FAILED";
}

} catch (Exception e) {

e.printStackTrace();
System.out.println(this.getClass().getName()
	+ " INFO: Error creating group aset Improvement>> "
		+ e.getMessage());
} finally {
	dbConnection.closeConnection(con, ps);
	dbConnection.closeConnection(con, ps2);
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
    
    public String getUsefullife()
    {
        return usefullife;
    }
    public void setUsefullife(String usefullife)
    {
        this.usefullife = usefullife;
    }   
    
    public String getOldcost_price()
    {
        return oldcost_price;
    }
    public void setOldcost_price(String oldcost_price)
    {
        this.oldcost_price = oldcost_price;
    }   
    
    public String getOldvatable_cost()
    {
        return oldvatable_cost;
    }
    public void setOldvatable_cost(String oldvatable_cost)
    {
        this.oldvatable_cost = oldvatable_cost;
    }   
    
    public String getOldaccum_dep()
    {
        return oldaccum_dep;
    }
    public void setOldaccum_dep(String oldaccum_dep)
    {
        this.oldaccum_dep = oldaccum_dep;
    }   
    
    public String getOldvat_amount()
    {
        return oldvat_amount;
    }
    public void setOldvat_amount(String oldvat_amount)
    {
        this.oldvat_amount = oldvat_amount;
    }   
    
    public String getOldnbv()
    {
        return oldnbv;
    }
    public void setOldnbv(String oldnbv)
    {
        this.oldnbv = oldnbv;
    }   
    
    public String getOldwht_amount()
    {
        return oldwht_amount;
    }
    public void setOldwht_amount(String oldwht_amount)
    {
        this.oldwht_amount = oldwht_amount;
    }   
    
    public String getOldIMPROV_NBV()
    {
        return oldIMPROV_NBV;
    }
    public void setOldIMPROV_NBV(String oldIMPROV_NBV)
    {
        this.oldIMPROV_NBV = oldIMPROV_NBV;
    }   
    
    public String getOldIMPROV_ACCUMDEP()
    {
        return oldIMPROV_ACCUMDEP;
    }
    public void setOldIMPROV_ACCUMDEP(String oldIMPROV_ACCUMDEP)
    {
        this.oldIMPROV_ACCUMDEP = oldIMPROV_ACCUMDEP;
    }       
    
    public String getOldIMPROV_COST()
    {
        return oldIMPROV_COST;
    }
    public void setOldIMPROV_COST(String oldIMPROV_COST)
    {
        this.oldIMPROV_COST = oldIMPROV_COST;
    }       
    
    public String getOldIMPROV_VATABLECOST()
    {
        return oldIMPROV_VATABLECOST;
    }
    public void setOldIMPROV_VATABLECOST(String oldIMPROV_VATABLECOST)
    {
        this.oldIMPROV_VATABLECOST = oldIMPROV_VATABLECOST;
    }       
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
    public int insertGroupAssetImproveRecordUpload(String groupId)
    throws Exception, Throwable
{
 //       asset_id = new legend.AutoIDSetup().getIdentity(branch_id,
 //               department_id, section_id, category_id);
  //      String codeno = approve.getCodeName("select mt_id from IA_MTID_TABLE where mt_tablename = 'AM_ASSET'");
//        String codeno =  new ApplicationHelper().getGeneratedId("AM_ASSET"); 
//        int newcode = Integer.parseInt(codeno);
//        String gid = approve.getCodeName("select mt_id from IA_MTID_TABLE where mt_tablename = 'am_group_asset_main'");
//    	String gid =  new ApplicationHelper().getGeneratedId("AM_GROUP_ASSET_MAIN"); 
        String gid = groupId+user_id; 
//        String asset_code = Integer.toString(newcode);
    String budget[] = getBudgetInfo();
    double bugdetvalues[] = getBudgetValues();
//    System.out.println("=====insertGroupAssetImproveRecordUpload asset_id=====> "+asset_id+"   gid: "+gid);
    int DONE = 0;
    int BUDGETENFORCED = 1;
    int BUDGETENFORCEDCF = 2;
    int ASSETPURCHASEBD = 3;
    int value = 0;
    String Q = getQuarter();
    if(budget[3].equalsIgnoreCase("N"))
    {
        createGroupUpload(asset_id,gid,asset_code);
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
                    createGroupUpload(asset_id,gid,asset_code);
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
                    createGroupUpload(asset_id,gid,asset_code);
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
            createGroupUpload(asset_id,gid,asset_code);
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
        }

        dbConnection.closeConnection(con, ps, rs);
//
//        dbConnection.closeConnection(con, ps, rs);
//        dbConnection.closeConnection(con, ps, rs);
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
        }
//        dbConnection.closeConnection(con, ps, rs);
//
//        dbConnection.closeConnection(con, ps, rs);

        dbConnection.closeConnection(con, ps, rs);
        return result;
    }

    public String getQuarter()
    {
        String quarter = "NQ";
        String budg[] = getBudgetInfo();
        double q1 = Double.parseDouble(budg[1]) / 4D;
        int month = dateFormat.getDayDifference(sdf.format(date_of_purchase.getTime()), budg[0]) / 30;
        boolean btw = dateFormat.isDateBetween(budg[0], budg[2], sdf.format(date_of_purchase.getTime()));
        if(btw)
        {
            if((double)month <= q1)
            {
                quarter = "FIRST";
//                System.out.println("1st Quarter");
            } else
            if((double)month > q1 && (double)month <= q1 * 2D)
            {
                quarter = "2ND";
//                System.out.println("2nd Quarter");
            } else
            if((double)month > q1 * 2D && (double)month <= q1 * 3D)
            {
                quarter = "3RD";
//                System.out.println("3rd Quarter");
            } else
            if((double)month > q1 * 3D)
            {
                quarter = "4TH";
//                System.out.println("4th Quarter");
            }
        }
        return quarter;
    }
/*
    public String getCatCode()
    {
        String query;
        String catid;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
        query = (new StringBuilder("SELECT CATEGORY_CODE  FROM am_ad_category  WHERE category_id = '")).append(category_id).append("' ").toString();
        catid = "0";
        try
        {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
 //           for(ResultSet rs = getStatement().executeQuery(query); rs.next();)
            	for(rs = ps.executeQuery(); rs.next();)
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
*/

    public String getCatCode()
    {
 //       double result[];
        String query;
        String catid;
        Connection con;
        PreparedStatement ps;
        ResultSet rs;
//        result = new double[8];
        query = (new StringBuilder("SELECT CATEGORY_CODE  FROM am_ad_category  WHERE category_id = '")).append(category_id).append("' ").toString();
        catid = "0";
        con = null;
        ps = null;
        rs = null;
        try
        {
            con = dbConnection.getConnection("legendPlus");
            ps = con.prepareStatement(query);
 //           for(ResultSet rs = getStatement().executeQuery(query); rs.next();)
            	for(rs = ps.executeQuery(); rs.next();)
            {
                catid = rs.getString(1);
            }
        }
        catch(Exception e)
        {
            String warning = (new StringBuilder("WARNING:Error Fetching Category Code  ->")).append(e.getMessage()).toString();
            System.out.println(warning);
        }
  //      dbConnection.closeConnection(con, ps, rs);
	    finally {
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

        }

        dbConnection.closeConnection(conn, stmt);

        dbConnection.closeConnection(conn, stmt);
        dbConnection.closeConnection(conn, stmt);
        System.out.println("Exiting update of Aquicisition Budget due to Reclassification");
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

    public void setNewcost_price(String newcost_price)
    {
        if(newcost_price != null)
        {
            this.newcost_price = newcost_price.replaceAll(",", "");
        }
    }

    public String getNewcost_price()
    {
        return newcost_price;
    }

    public void setNewvatable_cost(String newvatable_cost)
    {
        if(newvatable_cost != null)
        {
            this.newvatable_cost = newvatable_cost.replaceAll(",", "");
        }
    }

    public String getNewvatable_cost()
    {
        return newvatable_cost;
    }

    public void setNewvat_amount(String newvat_amount)
    {
        if(newvat_amount != null)
        {
            this.newvat_amount = newvat_amount.replaceAll(",", "");
        }
    }

    public String getNewvat_amount()
    {
        return newvat_amount;
    }

    public void setNewnbv(String newnbv)
    {
        if(newnbv != null)
        {
            this.newnbv = newnbv.replaceAll(",", "");
        }
    }

    public String getNewnbv()
    {
        return newnbv;
    }  

    public void setNewwht_amount(String newwht_amount)
    {
        if(newwht_amount != null)
        {
            this.newwht_amount = newwht_amount.replaceAll(",", "");
        }
    }

    public String getNewwht_amount()
    {
        return newwht_amount;
    }    
    
}
