package legend.admin.objects;


public class vendorCriteria
{

    private String id;
    private String criteriaCode;
    private String criteria;
    private String status;
    private String userId;
    private String menuType;
    private String createDate;

    public vendorCriteria()
    {
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getCriteriaCode()
    {
        return criteriaCode;
    }

    public void setCriteriaCode(String criteriaCode)
    {
        this.criteriaCode = criteriaCode;
    }

    public String getCriteria()
    {
        return criteria;
    }

    public void setCriteria(String criteria)
    {
        this.criteria = criteria;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getMenuType()
    {
        return menuType;
    }

    public void setMenuType(String menuType)
    {
        this.menuType = menuType;
    }

    public String getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(String createDate)
    {
        this.createDate = createDate;
    }
}
