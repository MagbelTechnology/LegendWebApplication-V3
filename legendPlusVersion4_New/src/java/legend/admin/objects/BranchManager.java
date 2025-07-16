



package legend.admin.objects;




public class BranchManager
{

    private String mtid;
    private String branchCode;
    private String branchName;
    private String managerName;
    private String emailAddress;
    private String status;
    private String create_date;
    private String userId;

    public BranchManager()
    {
    }

    public BranchManager(String mtid, String branchCode, String managerName, String emailAddress,String status,String create_date,String userId)
    {
        this.mtid = mtid;
        this.branchCode = branchCode;
        this.managerName = managerName;
        this.emailAddress = emailAddress;
        this.status = status;
        this.create_date = create_date;
        this.userId = userId;
    }
    //Vickie - create accessor methods
     /**
     * @return the emailAddress
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * @param emailAddress the emailAddress to set
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }


    public void setMtid(String mtid)
    {
        this.mtid = mtid;
    }

    public String getMtid()
    {
        return mtid;
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

    public void setManagerName(String managerName)
    {
        this.managerName = managerName;
    }

    public String getManagerName()
    {
        return managerName;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public void setCreate_date(String create_date)
    {
        this.create_date = create_date;
    }

    public String getCreate_date()
    {
        return create_date;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUserId()
    {
        return userId;
    }

}

