package com.magbel.ia.vao;

public class InventoryTotal {
    
    private String mtId;
    private String categoryCode;
    private String itemCode;
    private int itemBalance;
    private int itemtmpBalance;
    private String desc;
    private int userId;
    private String wareHouseCode;
    private int itemBalanceLtd;
    private int itemBalanceYtd;
    
    public InventoryTotal(String mtId,String itemCode,int itemBalance,String desc,String wareHouseCode,int userId,int itemBalanceYtd,int itemBalanceLtd) {
    
    setMtId(mtId);
    setItemCode(itemCode);
    setItemBalance(itemBalance);
    setDesc(desc);
    setUserId(userId);
    setWareHouseCode(wareHouseCode);
    setItemBalanceYtd(itemBalanceYtd);
    setItemBalanceLtd(itemBalanceLtd);
    
    } 
    public InventoryTotal() {
        
    setMtId(mtId);
    setItemCode(itemCode);
    setItemBalance(itemBalance);
    setDesc(desc);
    setUserId(userId);
    setWareHouseCode(wareHouseCode);
    setItemBalanceYtd(itemBalanceYtd);
    setItemBalanceLtd(itemBalanceLtd);
    
    } 

    public void setMtId(String mtId) {
        this.mtId = mtId;
    }

    public String getMtId() {
        return mtId;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemBalance(int itemBalance) {
        this.itemBalance = itemBalance;
    }

    public int getItemBalance() {
        return itemBalance;
    }

    public void setItemtmpBalance(int itemtmpBalance) {
        this.itemtmpBalance = itemtmpBalance;
    }

    public int getItemtmpBalance() {
        return itemtmpBalance;
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

    public void setWareHouseCode(String wareHouseCode) {
        this.wareHouseCode = wareHouseCode;
    }

    public String getWareHouseCode() {
        return wareHouseCode;
    }

    public void setItemBalanceLtd(int itemBalanceLtd) {
        this.itemBalanceLtd = itemBalanceLtd;
    }

    public int getItemBalanceLtd() {
        return itemBalanceLtd;
    }

    public void setItemBalanceYtd(int itemBalanceYtd) {
        this.itemBalanceYtd = itemBalanceYtd;
    }

    public int getItemBalanceYtd() {
        return itemBalanceYtd;
    }
}
