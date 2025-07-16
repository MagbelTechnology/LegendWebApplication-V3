package com.magbel.ia.vao;

public class SalesOrderDeliveryItem {

    private String id;
    private String orderCode;
    private int quantity;
    private double unitPrice;
    private double amount;
    private double advancePymt;
    private String itemCode;
    private int quantDeliver;
    private int quantRemain;
    private String batchCode;
    private String transDate;
    private int userId;
    private String customerCode;
    private String warehouseCode;
    
    public SalesOrderDeliveryItem(String id,String orderCode,int quantity,
    double unitPrice,double amount,double advancePymt,String itemCode,int quantDeliver,
    int quantRemain,String batchCode,String transDate,int userId,String customerCode,String warehouseCode) {
    
        setId(id);
        setOrderCode(orderCode);
        setQuantity(quantity);
        setUnitPrice(unitPrice);
        setAmount(amount);
        setAdvancePymt(advancePymt);
        setItemCode(itemCode);
        setQuantDeliver(quantDeliver);
        setQuantRemain(quantRemain);
        setBatchCode(batchCode);
        setTransDate(transDate);
        setUserId(userId);
        setCustomerCode(customerCode);
        setWarehouseCode(warehouseCode);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAdvancePymt(double advancePymt) {
        this.advancePymt = advancePymt;
    }

    public double getAdvancePymt() {
        return advancePymt;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setQuantDeliver(int quantDeliver) {
        this.quantDeliver = quantDeliver;
    }

    public int getQuantDeliver() {
        return quantDeliver;
    }

    public void setQuantRemain(int quantRemain) {
        this.quantRemain = quantRemain;
    }

    public int getQuantRemain() {
        return quantRemain;
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

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }
}
