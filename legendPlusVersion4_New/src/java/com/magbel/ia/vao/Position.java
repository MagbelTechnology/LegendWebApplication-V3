package com.magbel.ia.vao;


public class Position
{

    private String id;
    private String code;
    private int currId;
    private String isoCode;
    private String lcPosAcc;
    private String fcPosAcc;
    private String revalGain;
    private String revalLoss;
    private String createDt;
    private int userId;

    public Position(String id, String code, int currId, String isoCode, String fcPosAcc, String lcPosAcc, String revalGain, 
            String revalLoss, String createDt, int userId)
    {
        setId(id);
        setCode(code);
        setCurrencyId(currId);
        setIsoCode(isoCode);
        setLoPosAcc(lcPosAcc);
        setFcPosAcc(fcPosAcc);
        setRevalueGain(revalGain);
        setRevalueLoss(revalLoss);
        setCreateDt(createDt);
        setUserId(userId);
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public void setCurrencyId(int currId)
    {
        this.currId = currId;
    }

    public void setIsoCode(String isoCode)
    {
        this.isoCode = isoCode;
    }

    public void setLoPosAcc(String lcPosAcc)
    {
        this.lcPosAcc = lcPosAcc;
    }

    public void setFcPosAcc(String fcPosAcc)
    {
        this.fcPosAcc = fcPosAcc;
    }

    public void setRevalueGain(String revalGain)
    {
        this.revalGain = revalGain;
    }

    public void setRevalueLoss(String revalLoss)
    {
        this.revalLoss = revalLoss;
    }

    public void setCreateDt(String createDt)
    {
        this.createDt = createDt;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getId()
    {
        return id;
    }

    public String getCode()
    {
        return code;
    }

    public int getCurrencyId()
    {
        return currId;
    }

    public String getIsoCode()
    {
        return isoCode;
    }

    public String getLoPosAcc()
    {
        return lcPosAcc;
    }

    public String getFcPosAcc()
    {
        return fcPosAcc;
    }

    public String getRevalueGain()
    {
        return revalGain;
    }

    public String getRevalueLoss()
    {
        return revalLoss;
    }

    public String getCreateDt()
    {
        return createDt;
    }

    public int getUserId()
    {
        return userId;
    }
}
