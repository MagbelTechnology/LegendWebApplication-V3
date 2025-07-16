package com.magbel.ia.vao;

public class SalesOrderItem {

    private String id;
    private String orderCode;
    private int quantity;
    private double unitPrice;
    private double amount;
    private double advancePymt;
    private String itemCode;
    private int quantDeliver;
    private int quantRemain;
    private String warehouseCode;
    
    public SalesOrderItem(String id,String orderCode,int quantity,
    double unitPrice,double amount,double advancePymt,String itemCode,
    int quantDeliver,int quantRemain,String warehouseCode) {
    
        setId(id);
        setOrderCode(orderCode);
        setQuantity(quantity);
        setUnitPrice(unitPrice);
        setAmount(amount);
        setAdvancePymt(advancePymt);
        setItemCode(itemCode);
        setQuantDeliver(quantDeliver);
        setQuantRemain(quantRemain);
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

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }
}
