package com.magbel.ia.vao;

/**
 * @author Rahman Oloritun
 * @update Bolanle M Sule.
 *@ Copyright (c) 2008 _ IAS
 * @version 1.00
 */

public class User {

	private String userId;

	private String userName;

	private String userFullName;

	private String legacySystemId;

	private String userClass;

	private String branch;

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

	//private String fleetAdmin;

	private String email;

	private String userStatus;
	
    private String approveLevel;
	
	private String companyCode;
	
	private String  isStorekeeper;
 
    private String branchRestrict;
    
    private String userRestrict;
    
    private String isStockAdministrator;

	public User(String userId, String userName, String userFullName,
			String legacySystemId, String userClass, String branch,
			String password, String phoneNo, String isSupervisor,
			String mustChangePwd, String loginStatus, String createdBy,
			String createDate, String pwdChanged, String pwdExpiry,
			String lastLogindate, String loginSystem, String email,
			String userStatus, String approveLevel, String companyCode,
			String isStorekeeper) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userFullName = userFullName;
		this.legacySystemId = legacySystemId;
		this.userClass = userClass;
		this.branch = branch;
		this.password = password;
		this.phoneNo = phoneNo;
		this.isSupervisor = isSupervisor;
		this.mustChangePwd = mustChangePwd;
		this.loginStatus = loginStatus;
		this.createdBy = createdBy;
		this.createDate = createDate;
		this.pwdChanged = pwdChanged;
		this.pwdExpiry = pwdExpiry;
		this.lastLogindate = lastLogindate;
		this.loginSystem = loginSystem;
		this.email = email;
		this.userStatus = userStatus;
		this.approveLevel = approveLevel;
		this.companyCode = companyCode;
		this.isStorekeeper = isStorekeeper;
		
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



	public User() {
		// TODO Auto-generated constructor stub
	}

	

	public User(String userId, String userName, String userFullName, String legacySystemId, String userClass, String branch, String password, 
                   String phoneNo, String isSupervisor, String mustChangePwd, String loginStatus, String createdBy, String createDate, 
                   String pwdChanged, String pwdExpiry, String lastLogindate, String loginSystem, String email, String userStatus, String approveLevel, String companyCode) {
		
		this.userId = userId;
		this.userName = userName;
		this.userFullName = userFullName;
		this.legacySystemId = legacySystemId;
		this.userClass = userClass;
		this.branch = branch;
		this.password = password;
		this.phoneNo = phoneNo;
		this.isSupervisor = isSupervisor;
		this.mustChangePwd = mustChangePwd;
		this.loginStatus = loginStatus;
		this.createdBy = createdBy;
		this.createDate = createDate;
		this.pwdChanged = pwdChanged;
		this.pwdExpiry = pwdExpiry;
		this.lastLogindate = lastLogindate;
		this.loginSystem = loginSystem;
		this.email = email;
		this.userStatus = userStatus;
        this.approveLevel = approveLevel;
		this.companyCode = companyCode;
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
		if(mustChangePwd==null || mustChangePwd=="")
			this.mustChangePwd = "N" ;
		else
		this.mustChangePwd = mustChangePwd;
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
		if(isSupervisor==null || isSupervisor=="")
			this.isSupervisor =  "N" ;
		else
		this.isSupervisor = isSupervisor;
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
		if(loginStatus==null || isSupervisor=="")
			this.loginStatus =  "0" ;
		else
		this.loginStatus = loginStatus;
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
	/*public void setFleetAdmin(String fleetAdmin) {
		if(fleetAdmin==null||fleetAdmin=="")
			this.fleetAdmin  ="N";
		else
			this.fleetAdmin = fleetAdmin;
	}*/

	/**
	 * @return the fleetAdmin
	 */
	/*public String getFleetAdmin() {
		return fleetAdmin;
	}*/

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

    public void setApproveLevel(String approveLevel) {
        this.approveLevel = approveLevel;
    }

    public String getApproveLevel() {
        return approveLevel;
    }
	
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCompanyCode() {
		return companyCode;
	}
    public String getBranchRestrict() {
        return branchRestrict;
    }

    public void setBranchRestrict(String branchRestrict) {
        this.branchRestrict = branchRestrict;
    }

    public String getUserRestrict() {
        return userRestrict;
    }

    public void setUserRestrict(String userRestrict) {
        this.userRestrict = userRestrict;
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
    
}
