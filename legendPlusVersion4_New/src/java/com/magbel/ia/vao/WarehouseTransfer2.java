package com.magbel.ia.vao;

public class WarehouseTransfer2 {

    private String mtId;
    private String itemCode;
    private String fromWarehouse;
    private String toWarehouse;
    private String transDate;
    private int quantity;
    private int userId;
    private String fromBranch;
    private String toBranch;
	private String companyCode;
    
    public WarehouseTransfer2(){
        
    }
    public WarehouseTransfer2(String mtId,String itemCode,String fromWarehouse,
                             String toWarehouse,String transDate,int quantity,int userId,String fromBranch,String toBranch,String companyCode) {
    setMtId(mtId);
    setItemCode(itemCode);
    setFromWarehouse(fromWarehouse);
    setToWarehouse(toWarehouse);
    setTransDate(transDate);
    setQuantity(quantity);
    setUserId(userId);
    setFromBranch(fromBranch);
    setToBranch(toBranch);
	setCompanyCode(companyCode);
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

    public void setFromWarehouse(String fromWarehouse) {
        this.fromWarehouse = fromWarehouse;
    }

    public String getFromWarehouse() {
        return fromWarehouse;
    }

    public void setToWarehouse(String toWarehouse) {
        this.toWarehouse = toWarehouse;
    }

    public String getToWarehouse() {
        return toWarehouse;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setFromBranch(String fromBranch) {
        this.fromBranch = fromBranch;
    }

    public String getFromBranch() {
        return fromBranch;
    }

    public void setToBranch(String toBranch) {
        this.toBranch = toBranch;
    }

    public String getToBranch() {
        return toBranch;
    }
	
	public void setCompanyCode(String companyCode)
	{
	   this.companyCode = companyCode;
    }
	public String getCompanyCode()
	{
	   return companyCode;
    }
}
