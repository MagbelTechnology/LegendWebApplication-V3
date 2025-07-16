package com.magbel.ia.vao;


public class Revaluation
{

    private String id;
    private int exchId;
    private int posId;
    private int revalRate;
    private String period;
    private String freq;
    private int trm;
    private String posDesc;
    private String lastRevalDt;
    private String nxtRevaDt;
    private String createDt;
    private String userId;

    public Revaluation(String id, int exchId, int posId, int revalRate, String period, String freq, int trm, 
            String posDesc, String lastRevalDt, String nxtRevaDt, String createDt, String userId)
    {
        setId(id);
        setExchId(exchId);
        setPosId(posId);
        setRevalRate(revalRate);
        setPeriod(period);
        setFreq(freq);
        setTrm(trm);
        setPosDesc(posDesc);
        setLastRevalDt(lastRevalDt);
        setNxtRevalDt(nxtRevaDt);
        setCreateDt(createDt);
        setUserId(userId);
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }

    public void setExchId(int exchId)
    {
        this.exchId = exchId;
    }

    public int getExchId()
    {
        return exchId;
    }

    public void setPosId(int posId)
    {
        this.posId = posId;
    }

    public int getPosId()
    {
        return posId;
    }

    public void setRevalRate(int revalRate)
    {
        this.revalRate = revalRate;
    }

    public int getRevalRate()
    {
        return revalRate;
    }

    public void setPeriod(String period)
    {
        this.period = period;
    }

    public String getPeriod()
    {
        return period;
    }

    public void setFreq(String freq)
    {
        this.freq = freq;
    }

    public String getFreq()
    {
        return freq;
    }

    public void setTrm(int trm)
    {
        this.trm = trm;
    }

    public int getTrm()
    {
        return trm;
    }

    public void setPosDesc(String posDesc)
    {
        this.posDesc = posDesc;
    }

    public String getPosDesc()
    {
        return posDesc;
    }

    public void setLastRevalDt(String lastRevalDt)
    {
        this.lastRevalDt = lastRevalDt;
    }

    public String getLastRevalDt()
    {
        return lastRevalDt;
    }

    public void setNxtRevalDt(String nxtRevaDt)
    {
        this.nxtRevaDt = nxtRevaDt;
    }

    public String getNxtRevalDt()
    {
        return nxtRevaDt;
    }

    public void setCreateDt(String createDt)
    {
        this.createDt = createDt;
    }

    public String getCreateDt()
    {
        return createDt;
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
