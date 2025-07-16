package legend.admin.objects;

/**
 * @author Rahman Oloritun
 * @version 1.00
 */
import java.util.Date; 

public class UserNoToken {

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

    public UserNoToken() {
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
}
