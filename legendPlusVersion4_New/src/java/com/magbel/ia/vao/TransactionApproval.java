package com.magbel.ia.vao;

public class TransactionApproval {
    private String mtId;
    private String transCode;
    private String transType;
    private String desc;
    private int userId;
    private int approveOfficer;
    private String transDate;
    private String status;
    private String reason;
    private String itemCode;
    private int maxApproveLevel;
    private int concurrence;
    private int quantity;
    
    public TransactionApproval(String mtId,String transCode,String transType, String desc,int userId,
                               int approveOfficer,String transDate,String status,String reason,
                               String itemCode,int maxApproveLevel,int concurrence,int quantity) {
        setMtId(mtId);
        setTransCode(transCode);
        setTransType(transType);
        setDesc(desc);
        setUserId(userId);
        setApproveOfficer(approveOfficer); 
        setTransDate(transDate);
        setStatus(status);
        setReason(reason);
        setItemCode(itemCode);
        setMaxApproveLevel(maxApproveLevel);
        setConcurrence(concurrence);
        setQuantity(quantity);
    }

    public void setMtId(String mtId) {
        this.mtId = mtId;
    }

    public String getMtId() {
        return mtId;
    }

    public void setTransCode(String transCode) {
        this.transCode = transCode;
    }

    public String getTransCode() {
        return transCode;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransType() {
        return transType;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setApproveOfficer(int approveOfficer) {
        this.approveOfficer = approveOfficer;
    }

    public int getApproveOfficer() {
        return approveOfficer;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setMaxApproveLevel(int maxApproveLevel) {
        this.maxApproveLevel = maxApproveLevel;
    }

    public int getMaxApproveLevel() {
        return maxApproveLevel;
    }

    public void setConcurrence(int concurrence) {
        this.concurrence = concurrence;
    }

    public int getConcurrence() {
        return concurrence;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
