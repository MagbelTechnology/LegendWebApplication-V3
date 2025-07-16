package com.magbel.ia.vao;

public class PurchaseOrderDelivery {

    private String id;
    private String batchCode;
    private String desc;
    private String transDate;
    
    public PurchaseOrderDelivery(String id,String batchCode,String desc,String transDate) {
    
    setId(id);
    setBatchCode(batchCode);
    setDesc(desc);
    setTransDate(transDate);
    
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransDate() {
        return transDate;
    }
}
