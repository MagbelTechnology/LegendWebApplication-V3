package magma.net.vao;


public class RFAReversal
{

    private String id;
    private String code;
    private String desc;
    private String cCode;
    private String startDt;
    private String endDt;
    private String amountPayable;
    private String amount;
    private String other;
    private String scCode;
    private String status;
    private String branchId;
    private String deptName;
    private String sbuCode;
    private String quantity; 
    private String requestName;
    private String projectSponsor;
    private String projectOwner;
    private String projectManager;
    private String projectFundBal;
    private String projectAmtUtilized;
    
    public RFAReversal()
    {
    }

    public RFAReversal(String id, String code, String desc, String cCode, String startDt, String amountPayable, 
            String amount, String scCode, String status)
    {
        this.id = id;
        this.code = code;
        this.desc = desc;
        this.cCode = cCode;
        this.startDt = startDt;
        this.amountPayable = amountPayable;
        this.amount = amount;
        this.scCode = scCode;
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

    public void setAmountPayable(String amountPayable)
    {
        this.amountPayable = amountPayable;
    }

    public void setAmount(String amount)
    {
        this.amount = amount;
    }

    public void setOther(String other)
    {
        this.other = other;
    }

    public void setScCode(String scCode)
    {
        this.scCode = scCode;
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

    public String getAmountPayable()
    {
        return amountPayable;
    }

    public String getAmount()
    {
        return amount;
    }

    public String getOther()
    {
        return other;
    }

    public String getScCode()
    {
        return scCode;
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

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }   

    public String getDeptName()
    {
        return deptName;
    }    

    public void setSbuCode(String sbuCode)
    {
        this.sbuCode = sbuCode;
    }   

    public String getSbuCode()
    {
        return sbuCode;
    }    

    public void setQuantity(String quantity)
    {
        this.quantity = quantity;
    }   

    public String getQuantity()
    {
        return quantity;
    }    

    public void setRequestName(String requestName)
    {
        this.requestName = requestName;
    }   

    public String getRequestName()
    {
        return requestName;
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
