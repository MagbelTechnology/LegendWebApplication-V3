package com.magbel.ia.vao;

/**
 * <p>
 * com.magbel.ia.vao.PoVendorItem.java Mar 26, 2007
 * </p>
 *
 * @author Rahman Oloritun
 *
 * @version 1.00
 */
public class PoVendorItem {

	private String itemCode;

	private String vendorCode;

	private String preference;

	private String status;

	private String description;

	private double cost;

	private String date;

	private int quantity;

	private double total;
	private String id;

	/**
	 * <p>
	 * Description: generic constructor
	 * </p>
	 *
	 */
	public PoVendorItem() {

	}

	public PoVendorItem(String id,String itemCode, String vendorCode, String preference,
			String status, String description, double cost, String date,
			int quantity, double total) {
		super();

		this.id = id;
		this.itemCode = itemCode;
		this.vendorCode = vendorCode;
		this.preference = preference;
		this.status = status;
		this.description = description;
		this.cost = cost;
		this.date = date;
		this.quantity = quantity;
		this.total = total;
	}

	public void setId(String id){
			this.id = id;
	}

	public String getId(){
			return this.id;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
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

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getPreference() {
		return preference;
	}

	public void setPreference(String preference) {
		this.preference = preference;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

}
