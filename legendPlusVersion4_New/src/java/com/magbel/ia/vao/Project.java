package com.magbel.ia.vao;


public class Project
{

    private String id;
    private String code;
    private String desc;
    private String cCode;
    private String startDt;
    private String endDt;
    private String cost;
    private String capital;
    private String other;
    private String transDt;
    private String status;
    private String branchId;
    private String departmentId;
    private String sbuCode;
    private String onlineNumber; 
    private String projectAcct;
    private String projectSponsor;
    private String projectOwner;
    private String projectManager;
    private String projectFundBal;
    private String projectAmtUtilized;
    
    public Project()
    {
    }

    public Project(String id, String code, String desc, String cCode, String startDt, String endDt, String cost, 
            String capital, String other, String transDt, String status)
    {
        this.id = id;
        this.code = code;
        this.desc = desc;
        this.cCode = cCode;
        this.startDt = startDt;
        this.endDt = endDt;
        this.cost = cost;
        this.capital = capital;
        this.other = other;
        this.transDt = transDt;
        this.status = status;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public void setCCode(String cCode)
    {
        this.cCode = cCode;
    }

    public void setStartDt(String startDt)
    {
        this.startDt = startDt;
    }

    public void setEndDt(String endDt)
    {
        this.endDt = endDt;
    }

    public void setCost(String cost)
    {
        this.cost = cost;
    }

    public void setCapital(String capital)
    {
        this.capital = capital;
    }

    public void setOther(String other)
    {
        this.other = other;
    }

    public void setTransDt(String transDt)
    {
        this.transDt = transDt;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getId()
    {
        return id;
    }

    public String getCode()
    {
        return code;
    }

    public String getDesc()
    {
        return desc;
    }

    public String getCCode()
    {
        return cCode;
    }

    public String getStartDt()
    {
        return startDt;
    }

    public String getEndDt()
    {
        return endDt;
    }

    public String getCost()
    {
        return cost;
    }

    public String getCapital()
    {
        return capital;
    }

    public String getOther()
    {
        return other;
    }

    public String getTransDt()
    {
        return transDt;
    }

    public String getStatus()
    {
        return status;
    }

    public void setBranchId(String branchId)
    {
        this.branchId = branchId;
    }   

    public String getBranchId()
    {
        return branchId;
    }    

    public void setDepartmentId(String departmentId)
    {
        this.departmentId = departmentId;
    }   

    public String getDepartmentId()
    {
        return departmentId;
    }    

    public void setSbuCode(String sbuCode)
    {
        this.sbuCode = sbuCode;
    }   

    public String getSbuCode()
    {
        return sbuCode;
    }    

    public void setOnlineNumber(String onlineNumber)
    {
        this.onlineNumber = onlineNumber;
    }   

    public String getOnlineNumber()
    {
        return onlineNumber;
    }    

    public void setProjectAcct(String projectAcct)
    {
        this.projectAcct = projectAcct;
    }   

    public String getProjectAcct()
    {
        return projectAcct;
    }    

    public void setProjectSponsor(String projectSponsor)
    {
        this.projectSponsor = projectSponsor;
    }   

    public String getProjectSponsor()
    {
        return projectSponsor;
    }    

    public void setProjectOwner(String projectOwner)
    {
        this.projectOwner = projectOwner;
    }   

    public String getProjectOwner()
    {
        return projectOwner;
    }    

    public void setProjectManager(String projectManager)
    {
        this.projectManager = projectManager;
    }   

    public String getProjectManager()
    {
        return projectManager;
    }    

    public void setProjectFundBal(String projectFundBal)
    {
        this.projectFundBal = projectFundBal;
    }   

    public String getProjectFundBal()
    {
        return projectFundBal;
    }    

    public void setProjectAmtUtilized(String projectAmtUtilized)
    {
        this.projectAmtUtilized = projectAmtUtilized;
    }   

    public String getProjectAmtUtilized()
    {
        return projectAmtUtilized;
    }    
}
