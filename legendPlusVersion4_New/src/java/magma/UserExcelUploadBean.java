package magma;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

public class UserExcelUploadBean extends ConnectionClass
{
/*	
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
*/
	private boolean tokenRequired;
	
    private String userId;
    private String userName;
    private String userFullName;
    private String legacySystemId;
    private String userClass;
    private String branch;
    private String branchCode;
    private String password;
    private String phoneNo;
    private String isSupervisor;
    private String mustChangePwd;
    private String loginStatus;
    private String createdBy;
    private String createDate;
    private String pwdChanged;
    private String pwdExpiry;
    private String lastLogindate;
    private String loginSystem;
    private String fleetAdmin;
    private String email;
    private String userStatus;
    private String branchRestrict;
    private int expiryDays;
    private String expiryDate;
    private Calendar expDate;
    private String apprvLevel;
    private String apprvLimit;
    private String postingRestricted;
    private String deptCode;
    private String tokenRequire;
    private String  isStorekeeper;
    private String approveLevel;
    private String isStockAdministrator;
    private String deptRestrict;
    private String underTaker;
    private String regionCode;
    private String zoneCode;
    private String regionRestrict;
    private String zoneRestrict;
    private String isFacilityAdministrator;
    private String isStoreAdministrator;
    private String gid;
 
    public UserExcelUploadBean()
        throws Exception
    {
    	userId = "";
    	userName = "";
    	userFullName = "";
    	legacySystemId = "";
    	userClass = "";
    	branch = "0";
    	branchCode = "";
        expDate = new GregorianCalendar();
        password = "";
        phoneNo = "";
        isSupervisor = "";
        mustChangePwd = "";
        loginStatus = "";
        gid = "1";
        createdBy = "";
        createDate = "";
        pwdChanged = "";
        pwdExpiry = "";
        lastLogindate = "";
        loginSystem = "";
        fleetAdmin = "";
        email = "";
        userStatus = "";
        branchRestrict = "";
        expiryDays = 0;
        expiryDate = "";
        regionCode = "";
    	zoneCode = "";
    	apprvLevel = "";
    	apprvLimit = "";
    	postingRestricted = "";
    	deptCode = "";
    	tokenRequire = "";
    	isStorekeeper = "";
        approveLevel = "";
        isStockAdministrator = "";
        deptRestrict = "";
        underTaker = "";
        regionCode = "";
        zoneCode = "";
        regionRestrict = "";
        zoneRestrict = "";
        isFacilityAdministrator = "";
        isStoreAdministrator = "";    	

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
        String query = "INSERT INTO am_gb_User(User_Name,Full_Name,Legacy_Sys_id,Branch,Password,Class)\tVALUES('";
        b.append(query);
        b.append("',");
        b.append(userName);                 
        b.append(",");
        b.append(userFullName);   
        b.append(",");
        b.append(legacySystemId);
        b.append(",");
        b.append(branchCode); 
        b.append(",");
        b.append(password);
        b.append(",");
        b.append(userClass);
        b.append("')");
        try
        {   
            getStatement().executeUpdate(b.toString());
        }
        catch(Exception r)
        {
            System.out.println((new StringBuilder("INFO: Error creating group User Upload >>")).append(r).toString());
        }
    } 

public void DeleteExistingUser()
throws Exception
{
	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	String id = "";

	/*
	 *              query = "INSERT INTO AM_GB_USER( User_Name, Full_Name, Legacy_Sys_Id, Class, Branch, Pass" +
"word, Phone_No, Is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserI" +
"d, Create_Date, Password_Expiry,Fleet_Admin, Email,User_id,branch_restriction, E" +
"xpiry_Days,Approval_Limit,dept_code,token_required,region_code,zone_code," +
"region_restriction,zone_restriction,Facility_Admin,Store_Admin)VALUES (?,?,?,?,?,?,?,?,?,?,?" +
",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
;
	*/
String query2 = "delete from am_gb_User where User_Name in (select USER_NAME from am_gb_User_Upload)";
String query  = "INSERT INTO am_gb_User_Upload(User_Id,User_Name,Full_Name,Legacy_Sys_id,Branch,Password,Class,"+
				"Phone_No, Is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserId," +
				"Create_Date, Fleet_Admin, Email,branch_restriction," +
				"Approval_Limit,dept_code,token_required,region_code,zone_code," +
				"region_restriction,zone_restriction,Facility_Admin,Store_Admin)VALUES (?,?,?,?,?,?,?,?,?,?,?" +
				",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
try {     
	con = getConnection();  
	ps = con.prepareStatement(query);
	ps.setInt(1, Integer.parseInt(userId));
	ps.setString(2, userName);
	ps.setString(3, userFullName);
	ps.setString(4, legacySystemId);
	ps.setString(5, branch);
	ps.setString(6, password);
	ps.setString(7, userClass);
	ps.setString(8, phoneNo);
	ps.setString(9, isSupervisor);
	ps.setString(10, mustChangePwd);
	ps.setString(11, loginStatus);
	ps.setString(12, userStatus);
	ps.setString(13, "0");
	ps.setString(14, createDate);
	ps.setString(15, fleetAdmin);
	ps.setString(16, email);
	ps.setString(17, branchRestrict);
	ps.setString(18, apprvLimit);
	ps.setString(19, deptCode);
	ps.setString(20, tokenRequire);
	ps.setString(21, regionCode);
	ps.setString(22, zoneCode);
	ps.setString(23, regionRestrict);
	ps.setString(24, zoneRestrict);
	ps.setString(25, isFacilityAdministrator);
	ps.setString(26, isStoreAdministrator);
    
	System.out.println("<<<<<<<DeleteExistingUser userName: "+userName+"   userFullName: "+userFullName+"   legacySystemId: "+legacySystemId+"  branchCode: "+branchCode+"   tranType: "+password);
	done = (ps.executeUpdate() != -1);
	freeResource(con, ps);
	con = getConnection();  
	ps = con.prepareStatement(query2);
	done = (ps.executeUpdate() != -1);
	freeResource(con, ps);
} catch (Exception e) {

e.printStackTrace();
System.out.println(this.getClass().getName()
	+ " INFO: Error in DeleteExistingUser creating User Upload>> "
		+ e.getMessage());
} finally {
	freeResource(con, ps);
}

}


public void createUserUpload()
throws Exception
{
	Connection con = null;
	PreparedStatement ps = null;
	boolean done = false;
	String id = "";

String query = "INSERT INTO am_gb_User_Upload(User_Id,User_Name,Full_Name,Legacy_Sys_id,Branch,Password,Class,"+
	"Phone_No, Is_Supervisor, Must_Change_Pwd, Login_Status, User_Status, UserId," +
	"Create_Date, Fleet_Admin, Email,branch_restriction," +
	"Approval_Limit,dept_code,token_required,region_code,zone_code," +
	"region_restriction,zone_restriction,Facility_Admin,Store_Admin)VALUES (?,?,?,?,?,?,?,?,?,?,?" +
	",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

try {   
	con = getConnection();  
	ps = con.prepareStatement(query);
	ps.setInt(1, Integer.parseInt(userId));
	ps.setString(2, userName);
	ps.setString(3, userFullName);
	ps.setString(4, legacySystemId);
	ps.setString(5, branch);
	ps.setString(6, password);
	ps.setString(7, userClass);
	ps.setString(8, phoneNo);
	ps.setString(9, isSupervisor);
	ps.setString(10, mustChangePwd);
	ps.setString(11, loginStatus);
	ps.setString(12, userStatus);
	ps.setString(13, "0");
	ps.setString(14, createDate);
	ps.setString(15, fleetAdmin);
	ps.setString(16, email);
	ps.setString(17, branchRestrict);
	ps.setString(18, apprvLimit);
	ps.setString(19, deptCode);
	ps.setString(20, tokenRequire);
	ps.setString(21, regionCode);
	ps.setString(22, zoneCode);
	ps.setString(23, regionRestrict);
	ps.setString(24, zoneRestrict);
	ps.setString(25, isFacilityAdministrator);
	ps.setString(26, isStoreAdministrator);
	
	System.out.println("<<<<<<<createUserUpload userName: "+userName+"   userFullName: "+userFullName+"   legacySystemId: "+legacySystemId+"  branchCode: "+branchCode+"   password: "+password+"    userClass: "+userClass); 
	System.out.println("<<<<<<<createUserUpload phoneNo: "+phoneNo+"   isSupervisor: "+isSupervisor+"   mustChangePwd: "+mustChangePwd+"  loginStatus: "+loginStatus+"   password: "+password+"    userStatus: "+userStatus);
	System.out.println("<<<<<<<createUserUpload createDate: "+createDate+"   fleetAdmin: "+fleetAdmin+"   email: "+email+"  branchRestrict: "+branchRestrict+"   apprvLimit: "+apprvLimit+"    deptCode: "+deptCode);
	System.out.println("<<<<<<<createUserUpload tokenRequire: "+tokenRequire+"   regionCode: "+regionCode+"   zoneCode: "+zoneCode+"  regionRestrict: "+regionRestrict+"   zoneRestrict: "+zoneRestrict+"    isFacilityAdministrator: "+isFacilityAdministrator+"   isStoreAdministrator: "+isStoreAdministrator);
	done = (ps.executeUpdate() != -1);
	System.out.println("<<<<<<<createUserUpload done: "+done);
	freeResource(con, ps);
} catch (Exception e) {

e.printStackTrace();
System.out.println(this.getClass().getName()
	+ " INFO: Error in createUserUpload creating User Upload>> "
		+ e.getMessage());
} finally {
	freeResource(con, ps);
}

}


public String getRegionCode() 
{
	return regionCode;
} 

public void setRegionCode(String regionCode) {
	this.regionCode = regionCode;
}

public String getZoneCode() 
{
	return zoneCode;
} 

public void setZoneCode(String zoneCode) {
	this.zoneCode = zoneCode;
}
	
  public String getDeptCode() 
  {
	return deptCode;
} 

public void setDeptCode(String deptCode) {
	this.deptCode = deptCode;
}
public String getApprvLevel() {
    return apprvLevel;
}

public void setApprvLevel(String apprvLevel) {
    this.apprvLevel = apprvLevel;
}

public String getApprvLimit() {
    return apprvLimit;
}

public void setApprvLimit(String apprvLimit) {
    this.apprvLimit = apprvLimit;
}

/**
 * @param userId
 *            the userId to set
 */
public void setUserId(String userId) {
    this.userId = userId;
}

/**
 * @return the userId
 */
public String getUserId() {
    return userId;
}

/**
 * @param userName
 *            the userName to set
 */
public void setUserName(String userName) {
    this.userName = userName;
}

/**
 * @return the userName
 */
public String getUserName() {
    return userName;
}

/**
 * @param userFullName
 *            the userFullName to set
 */
public void setUserFullName(String userFullName) {
    this.userFullName = userFullName;
}

/**
 * @return the userFullName
 */
public String getUserFullName() {
    return userFullName;
}

/**
 * @param legacySystemId
 *            the legacySystemId to set
 */
public void setLegacySystemId(String legacySystemId) {
    this.legacySystemId = legacySystemId;
}

/**
 * @return the legacySystemId
 */
public String getLegacySystemId() {
    return legacySystemId;
}

/**
 * @param userClass
 *            the userClass to set
 */
public void setUserClass(String userClass) {
    this.userClass = userClass;
}

/**
 * @return the userClass
 */
public String getUserClass() {
    return userClass;
}

/**
 * @param branch
 *            the branch to set
 */
public void setBranch(String branch) {
    this.branch = branch;
}

/**
 * @return the branch
 */
public String getBranch() {
    return branch;
} 

/**
 * @param branchCode
 *            the branchCode to set
 */
public void setBranchCode(String branchCode) {
    this.branchCode = branchCode;
}

/**
 * @return the branchCode
 */
public String getBranchCode() {
    return branchCode;
} 

/**
 * @param password
 *            the password to set
 */
public void setPassword(String password) {
    this.password = password;
}

/**
 * @return the password
 */
public String getPassword() {
    return password;
}

/**
 * @param phoneNo
 *            the phoneNo to set
 */
public void setPhoneNo(String phoneNo) {
    this.phoneNo = phoneNo;
}

/**
 * @return the phoneNo
 */
public String getPhoneNo() {
    return phoneNo;
}

/**
 * @param mustChangePwd
 *            the mustChangePwd to set
 */
public void setMustChangePwd(String mustChangePwd) {
    if (mustChangePwd == null || mustChangePwd == "") {
        this.mustChangePwd = "N";
    } else {
        this.mustChangePwd = mustChangePwd;
    }
}

/**
 * @return the mustChangePwd
 */
public String getMustChangePwd() {
    return mustChangePwd;
}

/**
 * @param isSupervisor
 *            the isSupervisor to set
 */
public void setIsSupervisor(String isSupervisor) {
    if (isSupervisor == null || isSupervisor == "") {
        this.isSupervisor = "N";
    } else {
        this.isSupervisor = isSupervisor;
    }
}

/**
 * @return the isSupervisor
 */
public String getIsSupervisor() {
    return isSupervisor;
}

/**
 * @param loginStatus
 *            the loginStatus to set
 */
public void setLoginStatus(String loginStatus) {
    if (loginStatus == null || isSupervisor == "") {
        this.loginStatus = "0";
    } else {
        this.loginStatus = loginStatus;
    }
}

/**
 * @return the loginStatus
 */
public String getLoginStatus() {
    return loginStatus;
}

/**
 * @param createdBy
 *            the createdBy to set
 */
public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
}

/**
 * @return the createdBy
 */
public String getCreatedBy() {
    return createdBy;
}

/**
 * @param createDate
 *            the createDate to set
 */
public void setCreateDate(String createDate) {
    this.createDate = createDate;
}

/**
 * @return the createDate
 */
public String getCreateDate() {
    return createDate;
}

/**
 * @param pwdChanged
 *            the pwdChanged to set
 */
public void setPwdChanged(String pwdChanged) {

    this.pwdChanged = pwdChanged;
}

/**
 * @return the pwdChanged
 */
public String getPwdChanged() {
    return pwdChanged;
}

/**
 * @param pwdExpiry
 *            the pwdExpiry to set
 */
public void setPwdExpiry(String pwdExpiry) {
    this.pwdExpiry = pwdExpiry;
}

/**
 * @return the pwdExpiry
 */
public String getPwdExpiry() {
    return pwdExpiry;
}

/**
 * @param lastLogindate
 *            the lastLogindate to set
 */
public void setLastLogindate(String lastLogindate) {
    this.lastLogindate = lastLogindate;
}

/**
 * @return the lastLogindate
 */
public String getLastLogindate() {
    return lastLogindate;
}

/**
 * @param loginSystem
 *            the loginSystem to set
 */
public void setLoginSystem(String loginSystem) {
    this.loginSystem = loginSystem;
}

/**
 * @return the loginSystem
 */
public String getLoginSystem() {
    return loginSystem;
}

/**
 * @param fleetAdmin
 *            the fleetAdmin to set
 */
public void setFleetAdmin(String fleetAdmin) {
    if (fleetAdmin == null || fleetAdmin == "") {
        this.fleetAdmin = "N";
    } else {
        this.fleetAdmin = fleetAdmin;
    }
}

/**
 * @return the fleetAdmin
 */
public String getFleetAdmin() {
    return fleetAdmin;
}

/**
 * @param email
 *            the email to set
 */
public void setEmail(String email) {
    this.email = email;
}

/**
 * @return the email
 */
public String getEmail() {
    return email;
}

/**
 * @param userStatus
 *            the userStatus to set
 */
public void setUserStatus(String userStatus) {
    this.userStatus = userStatus;
}

/**
 * @return the userStatus
 */
public String getUserStatus() {
    return userStatus;
}

/**
 * @return the branchRestrict
 */
public String getBranchRestrict() {
    return branchRestrict;
}

/**
 * @param branchRestrict the branchRestrict to set
 */
public void setBranchRestrict(String branchRestrict) {
    this.branchRestrict = branchRestrict;
}

/**
 * @return the expiryDays
 */
public int getExpiryDays() {
    return expiryDays;
}

/**
 * @param expiryDays the expiryDays to set
 */
public void setExpiryDays(int expiryDays) {
    this.expiryDays = expiryDays;
}

/**
 * @return the expiryDate
 */
public String getExpiryDate() {
    return expiryDate;
}

/**
 * @param expiryDate the expiryDate to set
 */
public void setExpiryDate(String expiryDate) {
    this.expiryDate = expiryDate;
}

/**
 * @return the expDate
 */

public String getExpDate()
{
    return DateManipulations.CalendarToDate(expDate);
}

/**
 * @param expDate the expDate to set
 */
public void setExpDate() {
    this.expDate = expDate;
}

/**
 * @return the postingRestricted
 */
public String getPostingRestricted() {
    return postingRestricted;
}
/**
 * @param postingRestricted the postingRestricted to set
 */
public void setPostingRestricted(String postingRestricted) {
    // this.postingRestricted = postingRestricted;


    if (postingRestricted == null || postingRestricted.equalsIgnoreCase("")) {
        this.postingRestricted = "N";
    } else {
        this.postingRestricted = postingRestricted;
    }
}

public boolean isTokenRequired() {
	return tokenRequired;
}

public void setTokenRequired(boolean tokenRequired) {
	this.tokenRequired = tokenRequired;
}

/**
 * @return the branchRestrict
 */
public String getTokenRequire() {
    return tokenRequire;
}

/**
 * @param branchRestrict the branchRestrict to set
 */
public void setTokenRequire(String tokenRequire) {
    this.tokenRequire = tokenRequire;
}

	public String getIsStorekeeper() {  
		return isStorekeeper;
	}

	public void setIsStorekeeper(String isStorekeeper) {
		if(isStorekeeper==null || isStorekeeper=="")
			this.isStorekeeper =  "N" ;
		else
		this.isStorekeeper = isStorekeeper;
	}

	public String getApproveLevel() {
		return approveLevel;
	}

public void setApproveLevel(String approveLevel) {
    this.approveLevel = approveLevel;
}
	
	public String getIsStockAdministrator() {
		return isStockAdministrator;
	}

	public void setIsStockAdministrator(String isStockAdministrator) {
		if(isStockAdministrator==null || isStockAdministrator=="")
			this.isStockAdministrator =  "N" ;
		else
		this.isStockAdministrator = isStockAdministrator;
	}   	

/**
 * @return the deptRestrict
 */
public String getDeptRestrict() {
    return deptRestrict;
}

/**
 * @param deptRestrict the deptRestrict to set
 */
public void setDeptRestrict(String deptRestrict) {
    this.deptRestrict = deptRestrict;
}

/**
 * @return the underTaker
 */
public String getUnderTaker() {
    return underTaker;
}

/**
 * @param underTaker the underTaker to set
 */
public void setUnderTaker(String underTaker) {
    this.underTaker = underTaker;
}    

/**
 * @return the regionRestrict
 */
public String getRegionRestrict() {
    return regionRestrict;
}

/**
 * @param regionRestrict the regionRestrict to set
 */
public void setRegionRestrict(String regionRestrict) {
    this.regionRestrict = regionRestrict;
}


/**
 * @return the zoneRestrict
 */
public String getZoneRestrict() {
    return zoneRestrict;
}

/**
 * @param zoneRestrict the zoneRestrict to set
 */
public void setZoneRestrict(String zoneRestrict) {
    this.zoneRestrict = zoneRestrict;
}
	
	public String getIsFacilityAdministrator() {
		return isFacilityAdministrator;
	}

	public void setIsFacilityAdministrator(String isFacilityAdministrator) {
		if(isFacilityAdministrator==null || isFacilityAdministrator=="")
			this.isFacilityAdministrator =  "N" ;
		else
		this.isFacilityAdministrator = isFacilityAdministrator;
	}   	
	
	public String getIsStoreAdministrator() {
		return isStoreAdministrator;
	}

	public void setIsStoreAdministrator(String isStoreAdministrator) {
		if(isStoreAdministrator==null || isStoreAdministrator=="")
			this.isStoreAdministrator =  "N" ;
		else
		this.isStoreAdministrator = isStoreAdministrator;
	}   	

    public String getGid()
    {
        return gid;
    }

    public void setGid(String gid)
    {
        this.gid = gid;
    }

/*
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
*/
    public int insertUserRecordUpload()  
    throws Exception, Throwable
{

    int DONE = 0;

    int value = 0;
    	DeleteExistingUser();
//        createUserUpload();
        value = DONE;
 
    return value;
}

    /*
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
*/ 
    
    
}
