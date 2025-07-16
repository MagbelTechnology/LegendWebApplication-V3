



package com.magbel.admin.objects;




public class Branch
{

    private String branchId;
    private String branchCode;
    private String branchName;
    private String branchAcronym;
    private String glPrefix;
    private String glSuffix;
    private String branchAddress;
    private String state;
    private String phoneNo;
    private String faxNo;
    private String region;
    private String province;
    private String branchStatus;
    private String username;
    private String create_date;
    private int location;

    //Vickie - create a new field for email
    private String emailAddress;

    private  String unClassified;

    public Branch()
    {
    }

    public Branch(String branchId, String branchCode, String branchName, String branchAcronym, String glPrefix, String glSuffix, String branchAddress,
            String state, String phoneNo, String faxNo, String region, String province, String branchStatus, String username,
            String create_date)
    {
        this.branchId = branchId;
        this.branchCode = branchCode;
        this.branchName = branchName;
        this.branchAcronym = branchAcronym;
        this.glPrefix = glPrefix;
        this.glSuffix = glSuffix;
        this.branchAddress = branchAddress;
        this.state = state;
        this.phoneNo = phoneNo;
        this.faxNo = faxNo;
        this.region = region;
        this.province = province;
        this.branchStatus = branchStatus;
        this.username = username;
        this.create_date = create_date;
    }

    //Vickie - Create an overloaded contructor to accept emailAddress
    public Branch(String branchId, String branchCode, String branchName, String branchAcronym, String glPrefix, String glSuffix, String branchAddress,
            String state, String phoneNo, String faxNo, String region, String province, String branchStatus, String username,
            String create_date, String emailAddress)
    {
        this.branchId = branchId;
        this.branchCode = branchCode;
        this.branchName = branchName;
        this.branchAcronym = branchAcronym;
        this.glPrefix = glPrefix;
        this.glSuffix = glSuffix;
        this.branchAddress = branchAddress;
        this.state = state;
        this.phoneNo = phoneNo;
        this.faxNo = faxNo;
        this.region = region;
        this.province = province;
        this.branchStatus = branchStatus;
        this.username = username;
        this.create_date = create_date;
        this.emailAddress = emailAddress;
    }
    public Branch(String branchId, String branchCode, String branchName, String branchAcronym, String glPrefix, String glSuffix, String branchAddress,
            String state, String phoneNo, String faxNo, String region, String province, String branchStatus, String username,
            String create_date, String emailAddress, String unClassified)
    {
        this.branchId = branchId;
        this.branchCode = branchCode;
        this.branchName = branchName;
        this.branchAcronym = branchAcronym;
        this.glPrefix = glPrefix;
        this.glSuffix = glSuffix;
        this.branchAddress = branchAddress;
        this.state = state;
        this.phoneNo = phoneNo;
        this.faxNo = faxNo;
        this.region = region;
        this.province = province;
        this.branchStatus = branchStatus;
        this.username = username;
        this.create_date = create_date;
        this.emailAddress = emailAddress;
        this.unClassified = unClassified;
    }
    public Branch(String branchId, String branchCode, String branchName,
			String branchAcronym, String glPrefix, String glSuffix,
			String branchAddress, String state, String phoneNo, String faxNo,
			String region, String province, String branchStatus,
			String username, String create_date, int location,
			String emailAddress, String unClassified) {
		super();
		this.branchId = branchId;
		this.branchCode = branchCode;
		this.branchName = branchName;
		this.branchAcronym = branchAcronym;
		this.glPrefix = glPrefix;
		this.glSuffix = glSuffix;
		this.branchAddress = branchAddress;
		this.state = state;
		this.phoneNo = phoneNo;
		this.faxNo = faxNo;
		this.region = region;
		this.province = province;
		this.branchStatus = branchStatus;
		this.username = username;
		this.create_date = create_date;
		this.location = location;
		this.emailAddress = emailAddress;
		this.unClassified = unClassified;
	}
    public String getUnClassified() {
		return unClassified;
	}

	public void setUnClassified(String unClassified) {
		this.unClassified = unClassified;
	}
    //Vickie - create accessor methods
     /**
     * @return the emailAddress
     */
    public String getEmailAddress() {
       if(emailAddress== null)emailAddress="";
        return emailAddress;
    }

    /**
     * @param emailAddress the emailAddress to set
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }


    public void setBranchId(String branchId)
    {
        this.branchId = branchId;
    }

    public String getBranchId()
    {
        return branchId;
    }

    public void setBranchCode(String branchCode)
    {
        this.branchCode = branchCode;
    }

    public String getBranchCode()
    {
        return branchCode;
    }

    public void setBranchName(String branchName)
    {
        this.branchName = branchName;
    }

    public String getBranchName()
    {
        return branchName;
    }

    public void setBranchAcronym(String branchAcronym)
    {
        this.branchAcronym = branchAcronym;
    }

    public String getBranchAcronym()
    {
        return branchAcronym;
    }

    public void setGlPrefix(String glPrefix)
    {
        this.glPrefix = glPrefix;
    }

    public String getGlPrefix()
    {
        return glPrefix;
    }

    public void setGlSuffix(String glSuffix)
    {
        this.glSuffix = glSuffix;
    }

    public String getGlSuffix()
    {
        return glSuffix;
    }

    public void setBranchAddress(String branchAddress)
    {
        this.branchAddress = branchAddress;
    }

    public String getBranchAddress()
    {
        return branchAddress;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getState()
    {
        return state;
    }

    public void setPhoneNo(String phoneNo)
    {
        this.phoneNo = phoneNo;
    }

    public String getPhoneNo()
    {
        return phoneNo;
    }

    public void setFaxNo(String faxNo)
    {
        this.faxNo = faxNo;
    }

    public String getFaxNo()
    {
        if(faxNo == null)faxNo ="";
        return faxNo;
    }

    public void setRegion(String region)
    {
        this.region = region;
    }

    public String getRegion()
    {
        return region;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public String getProvince()
    {
        return province;
    }

    public void setBranchStatus(String branchStatus)
    {
        this.branchStatus = branchStatus;
    }

    public String getBranchStatus()
    {
        return branchStatus;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getUsername()
    {
        return username;
    }

    public void setCreate_date(String create_date)
    {
        this.create_date = create_date;
    }

    public String getCreate_date()
    {
        return create_date;
    }

    /**
     * @return the location
     */
    public int getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(int location) {
        this.location = location;
    }


}

