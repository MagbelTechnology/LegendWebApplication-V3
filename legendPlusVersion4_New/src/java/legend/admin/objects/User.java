package legend.admin.objects;

/**
 * @author Rahman Oloritun
 * @version 1.00
 */
import java.util.Date; 

public class User {

	
	/**
	 * Kazeem
	 * Add property tokenRequired(boolean) to determine if User will require hardware token to login
	 * 
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
    private Date expDate;
    private String apprvLevel;
    private String apprvLimit;
    private String postingRestricted;
    private String deptCode;
    private String sectionCode;
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
	
    public String getSectionCode() 
    {
		return sectionCode;
	} 

	public void setSectionCode(String sectionCode) {
		this.sectionCode = sectionCode;
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

    public User() {
        // TODO Auto-generated constructor stub
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
    public Date getExpDate() {
        return expDate;
    }

    /**
     * @param expDate the expDate to set
     */
    public void setExpDate(Date expDate) {
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
   
}
