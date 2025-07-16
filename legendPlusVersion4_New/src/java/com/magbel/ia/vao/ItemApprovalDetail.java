package com.magbel.ia.vao;



public class ItemApprovalDetail {

    private String mtId;
    private String itemCode;
    private int organPosition;
    private int userId;
    private String transDate;
    private int concurrence;
    private double minAmtLimit;
    private double maxAmtLimit;
    
    public ItemApprovalDetail(String mtId,String itemCode,int organPosition,int userId,
                              String transDate,int concurrence,double minAmtLimit,double maxAmtLimit) {
    setMtId(mtId);
    setItemCode(itemCode);
    setOrganPosition(organPosition);
    setUserId(userId);
    setTransDate(transDate);
    setConcurrence(concurrence);
    setMinAmtLimit(minAmtLimit);
    setMaxAmtLimit(maxAmtLimit);

    }

    public void setMtId(String mtId) {
        this.mtId = mtId;
    }

    public String getMtId() {
        return mtId;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setOrganPosition(int organPosition) {
        this.organPosition = organPosition;
    }

    public int getOrganPosition() {
        return organPosition;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setConcurrence(int concurrence) {
        this.concurrence = concurrence;
    }

    public int getConcurrence() {
        return concurrence;
    }

    public void setMinAmtLimit(double minAmtLimit) {
        this.minAmtLimit = minAmtLimit;
    }

    public double getMinAmtLimit() {
        return minAmtLimit;
    }

    public void setMaxAmtLimit(double maxAmtLimit) {
        this.maxAmtLimit = maxAmtLimit;
    }

    public double getMaxAmtLimit() {
        return maxAmtLimit;
    }
}
