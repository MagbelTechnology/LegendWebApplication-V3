package com.magbel.ia.vao;


public class Rate
{
        
    private String id;
    private int ptid;
    private String rate;
    private String userId;
    private String createDate;
    private String rateEffectiveDate;
    private String description;
    private String accountPayable;
    private String accountReceivable;
    private String staff;
    private String effectiveDate;
    private String status;
    private String rateCode;
    private String indexName;

    public Rate()
    {
    }

    public Rate(String id, int ptid, String rate, String userId, String createDate, String rateEffectiveDate, String description, 
            String accountPayable, String accountReceivable, String staff, String effectiveDate, String status, String rateCode, String indexName)
    {
        this.id = id;
        this.ptid = ptid;
        this.rate = rate;
        this.userId = userId;
        this.createDate = createDate;
        this.rateEffectiveDate = rateEffectiveDate;
        this.description = description;
        this.accountPayable = accountPayable;
        this.accountReceivable = accountReceivable;
        this.staff = staff;
        this.effectiveDate = effectiveDate;
        this.status = status;
        this.rateCode = rateCode;
        this.indexName = indexName;
    }

    public String getAccountPayable()
    {
        return accountPayable;
    }

    public void setAccountPayable(String accountPayable)
    {
        this.accountPayable = accountPayable;
    }

    public String getAccountReceivable()
    {
        return accountReceivable;
    }

    public void setAccountReceivable(String accountReceivable)
    {
        this.accountReceivable = accountReceivable;
    }

    public String getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(String createDate)
    {
        this.createDate = createDate;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getEffectiveDate()
    {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate)
    {
        this.effectiveDate = effectiveDate;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public int getPtid()
    {
        return ptid;
    }

    public void setPtid(int ptid)
    {
        this.ptid = ptid;
    }

    public String getRate()
    {
        return rate;
    }

    public void setRate(String rate)
    {
        this.rate = rate;
    }

    public String getRateEffectiveDate()
    {
        return rateEffectiveDate;
    }

    public void setRateEffectiveDate(String rateEffectiveDate)
    {
        this.rateEffectiveDate = rateEffectiveDate;
    }

    public String getStaff()
    {
        return staff;
    }

    public void setStaff(String staff)
    {
        this.staff = staff;
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

    public String getRateCode()
    {
        return rateCode;
    }

    public void setRateCode(String rateCode)
    {
        this.rateCode = rateCode;
    }

    public String getIndexName()
    {
        return indexName;
    }

    public void setIndexName(String indexName)
    {
        this.indexName = indexName;
    }
}
