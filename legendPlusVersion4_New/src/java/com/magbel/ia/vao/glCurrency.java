package com.magbel.ia.vao;


public class glCurrency
{
   
    private String id;
    private String currency_id;
    private String iso_code;
    private String createDate;
    private String ledgerId;
    private String effectiveDate;
    private String userId;
    private String ledgerNo;
    private String status;

    public glCurrency()
    {
    }

    public glCurrency(String id, String currency_id, String iso_code, String createDate, String ledgerId, String effectiveDate, String userId, 
            String ledgerNo, String status)
    {
        this.id = id;
        this.currency_id = currency_id;
        this.iso_code = iso_code;
        this.createDate = createDate;
        this.ledgerId = ledgerId;
        this.effectiveDate = effectiveDate;
        this.userId = userId;
        this.ledgerNo = ledgerNo;
        this.status = status;
    }

    public String getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(String createDate)
    {
        this.createDate = createDate;
    }

    public String getCurrency_id()
    {
        return currency_id;
    }

    public void setCurrency_id(String currency_id)
    {
        this.currency_id = currency_id;
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

    public String getIso_code()
    {
        return iso_code;
    }

    public void setIso_code(String iso_code)
    {
        this.iso_code = iso_code;
    }

    public String getLedgerId()
    {
        return ledgerId;
    }

    public void setLedgerId(String ledgerId)
    {
        this.ledgerId = ledgerId;
    }

    public String getLedgerNo()
    {
        return ledgerNo;
    }

    public void setLedgerNo(String ledgerNo)
    {
        this.ledgerNo = ledgerNo;
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
}
