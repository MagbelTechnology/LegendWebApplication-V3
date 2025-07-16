package com.magbel.ia.vao;

public class InventoryHistory {

    private String  mtId =  null;
      private String  itemCode  =  null;
      private String  desc  =  null;
      private String  transDesc   =  null;
      private int  quantity    =  0;
      private  String  wareHouseCode  = null;
      private  String  transDate = null;
      private  int  userId  =  0;
      private String batchCode;
     
     public InventoryHistory(String  mtId, String  itemCode, String  desc, 
                             String  transDesc, int  Quantity,String  wareHouseCode, 
                             String  transDate,  int  UserId,String batchCode)
    {
            this.mtId  =  mtId;
            this.itemCode  =  itemCode;
            this.desc  =  desc;
            this.transDesc  =  transDesc;
            this.quantity  = Quantity;
            this.wareHouseCode  =  wareHouseCode;
            this.transDate  =  transDate;
            this.userId  =  UserId;
            this.batchCode = batchCode;
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

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setTransDesc(String transDesc) {
        this.transDesc = transDesc;
    }

    public String getTransDesc() {
        return transDesc;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setWareHouseCode(String wareHouseCode) {
        this.wareHouseCode = wareHouseCode;
    }

    public String getWareHouseCode() {
        return wareHouseCode;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransDate() {
        return transDate;
    }
}
