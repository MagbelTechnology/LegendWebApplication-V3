package com.magbel.ia.vao;


public class ExchangeRate2
{

    private String MTID;
    private String currency_id;
    private String create_dt;
    private String effective_dt;
    private String exchg_rate;
    private String user_id;
    private String status;
    private String method;

    public ExchangeRate2()
    {
    }

    public ExchangeRate2(String MTID, String currency_id, String create_dt, String effective_dt, String exchg_rate, String user_id, String status, 
            String method)
    {
        this.MTID = MTID;
        this.currency_id = currency_id;
        this.create_dt = create_dt;
        this.effective_dt = effective_dt;
        this.exchg_rate = exchg_rate;
        this.user_id = user_id;
        this.status = status;
        this.method = method;
    }

    public void setMTID(String MTID)
    {
        this.MTID = MTID;
    }

    public String getMTID()
    {
        return MTID;
    }

    public void setCurrency_id(String currency_id)
    {
        this.currency_id = currency_id;
    }

    public void setCreate_dt(String create_dt)
    {
        this.create_dt = create_dt;
    }

    public void setEffective_dt(String effective_dt)
    {
        this.effective_dt = effective_dt;
    }

    public void setExchg_rate(String exchg_rate)
    {
        this.exchg_rate = exchg_rate;
    }

    public void setUser_id(String user_id)
    {
        this.user_id = user_id;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public void setMethod(String method)
    {
        this.method = method;
    }

    public String getCurrency_id()
    {
        return currency_id;
    }

    public String getCreate_dt()
    {
        return create_dt;
    }

    public String getEffective_dt()
    {
        return effective_dt;
    }

    public String getExchg_rate()
    {
        return exchg_rate;
    }

    public String getUser_id()
    {
        return user_id;
    }

    public String getStatus()
    {
        return status;
    }

    public String getMethod()
    {
        return method;
    }
}
