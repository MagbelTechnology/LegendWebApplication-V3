package magma.net.vao;

import java.io.Serializable;

public class BranchVisit implements Serializable {
    private String id;
    private String sNo;
    private String branchCode;
    private String dateInspect;
    private String inspectedBy;
    private String visitsummary;
    private String transDate;
    private String element;
    private String condition;
    private String remark;
    private String actionby;
    private String dueDate;
    private String status;
    private String userId; 
    
    public BranchVisit(){};
    
    public BranchVisit(String id, String branchCode, String dateInspect,String inspectedBy, String transDate, String visitsummary) {
        setId(id);
        setBranchCode(branchCode);
        setDateInspect(dateInspect);
        setInspectedBy(inspectedBy);
        setTransDate(transDate);
        setVisitsummary(visitsummary);
        
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setSNo(String sNo) {
        this.sNo = sNo;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getId() {
        return id;
    }

    public String getSNo() {
        return sNo;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setVisitsummary(String visitsummary) {
        this.visitsummary = visitsummary;
    }

    public String getVisitsummary() {
        return visitsummary;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getElement() {
        return element;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setActionby(String actionby) {
        this.actionby = actionby;
    }

    public String getActionby() {
        return actionby;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setDateInspect(String dateInspect) {
        this.dateInspect = dateInspect;
    }

    public String getDateInspect() {
        return dateInspect;
    }

    public void setInspectedBy(String inspectedBy) {
        this.inspectedBy = inspectedBy;
    }

    public String getInspectedBy() {
        return inspectedBy;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }        

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }  
    
}
