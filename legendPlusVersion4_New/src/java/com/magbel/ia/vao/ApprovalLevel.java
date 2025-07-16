package com.magbel.ia.vao;


public class ApprovalLevel
{

    private String id;
    private String code;
    private double minAmt;
    private double maxAmt;
    private String desc;
    private int adjMin;
    private int adjMax;
    private int adjCon;
    private int concur;

    public ApprovalLevel(String id, String code, double minAmt, double maxAmt, String desc, 
            int adjMin, int adjMax, int adjCon, int concur)
    {
        setId(id);
        setCode(code);
        setMinAmt(minAmt);
        setMaxAmt(maxAmt);
        setDesc(desc);
        setAdjMin(adjMin);
        setAdjMax(adjMax);
        setAdjCon(adjCon);
        setConcur(concur);
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public void setMinAmt(double minAmt)
    {
        this.minAmt = minAmt;
    }

    public void setMaxAmt(double maxAmt)
    {
        this.maxAmt = maxAmt;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public void setAdjMin(int adjMin)
    {
        this.adjMin = adjMin;
    }

    public void setAdjMax(int adjMax)
    {
        this.adjMax = adjMax;
    }

    public void setAdjCon(int adjCon)
    {
        this.adjCon = adjCon;
    }

    public void setConcur(int concur)
    {
        this.concur = concur;
    }

    public String getId()
    {
        return id;
    }

    public String getCode()
    {
        return code;
    }

    public double getMinAmt()
    {
        return minAmt;
    }

    public double getMaxAmt()
    {
        return maxAmt;
    }

    public String getDesc()
    {
        return desc;
    }

    public int getAdjMin()
    {
        return adjMin;
    }

    public int getAdjMax()
    {
        return adjMax;
    }

    public int getAdjCon()
    {
        return adjCon;
    }

    public int getConcur()
    {
        return concur;
    }
}
