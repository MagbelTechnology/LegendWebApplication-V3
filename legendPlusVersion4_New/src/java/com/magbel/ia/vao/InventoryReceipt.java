package com.magbel.ia.vao;

/**
 * <p>
 * com.magbel.ia.vao.InventoryReceipt.java Mar 26, 2007
 * </p>
 *
 * @author Rahman Oloritun
 *
 * @version 1.00
 *
 */
public class InventoryReceipt {

	private String id;
	private String orderNo;

	private String receiptNo;

	private String vendorCode;

	private String date;

	private int period;

	private int quantityReceived;

	private int quantityOrder;

	private String vendorItemNo;

	private String description;

	private double unitCost;

	private String overrideDescription;

	/**
	 *
	 */
	public InventoryReceipt() {

	}

	public InventoryReceipt(String id,String orderNo, String receiptNo,
			String vendorCode, String date, int period, int quantityReceived,
			int quantityOrder, String vendorItemNo, String description,
			double unitCost, String overrideDescription) {

		this.id = id;
		this.orderNo = orderNo;
		this.receiptNo = receiptNo;
		this.vendorCode = vendorCode;
		this.date = date;
		this.period = period;
		this.quantityReceived = quantityReceived;
		this.quantityOrder = quantityOrder;
		this.vendorItemNo = vendorItemNo;
		this.description = description;
		this.unitCost = unitCost;
		this.overrideDescription = overrideDescription;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOverrideDescription() {
		return overrideDescription;
	}

	public void setOverrideDescription(String overrideDescription) {
		this.overrideDescription = overrideDescription;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getId(){
		return this.id;
	}

	public int getQuantityOrder() {
		return quantityOrder;
	}

	public void setQuantityOrder(int quantityOrder) {
		this.quantityOrder = quantityOrder;
	}

	public int getQuantityReceived() {
		return quantityReceived;
	}

	public void setQuantityReceived(int quantityReceived) {
		this.quantityReceived = quantityReceived;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public double getUnitCost() {
		return unitCost;
	}

	public void setUnitCost(double unitCost) {
		this.unitCost = unitCost;
	}

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	public String getVendorItemNo() {
		return vendorItemNo;
	}

	public void setVendorItemNo(String vendorItemNo) {
		this.vendorItemNo = vendorItemNo;
	}

}
