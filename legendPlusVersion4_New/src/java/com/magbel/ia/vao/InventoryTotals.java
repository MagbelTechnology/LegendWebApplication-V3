package com.magbel.ia.vao;

public class InventoryTotals {
    
    private String mtId;
    private String itemCode;
    private int itemBalance;
    private String desc;
	private String wareHouseCode;
    private int userId;
    
    public InventoryTotals(String mtId,String itemCode,int itemBalance,String desc,String wareHouseCode,int userId) {
    
    setMtId(mtId);
    setItemCode(itemCode);
    setItemBalance(itemBalance);
    setDesc(desc);
    setWareHouseCode(wareHouseCode);
    setUserId(userId);
  
    
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

    public void setItemBalance(int itemBalance) {
        this.itemBalance = itemBalance;
    }

    public int getItemBalance() {
        return itemBalance;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
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
}
