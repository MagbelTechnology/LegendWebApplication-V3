package com.magbel.ia.vao;

/**
 * <p>
 * com.magbel.ia.vao.Shipment.java Mar 26, 2007
 * </p>
 *
 * @author Rahman Oloritun
 *
 * @version 1.00
 *
 */
public class Shipment {

	private String orderNo;

	private String printed;

	private String customerNo;

	private String shipTo;

	private String warehouse;

	private String date;

	private int period;

	private String freight;

	private String carrier;

	private int quantityShipped;

	private int quantityOrdered;

	private String itemNo;

	private String description;

	private double unitPrice;
	private String id;

	public Shipment() {

	}

	/**
	 * @param orderNo
	 * @param printed
	 * @param customerNo
	 * @param shipTo
	 * @param warehouse
	 * @param date
	 * @param period
	 * @param freight
	 * @param carrier
	 * @param quantityShipped
	 * @param quantityOrdered
	 * @param itemNo
	 * @param description
	 * @param unitPrice
	 */
	public Shipment(String id,String orderNo, String printed, String customerNo,
			String shipTo, String warehouse, String date, int period,
			String freight, String carrier, int quantityShipped,
			int quantityOrdered, String itemNo, String description,
			double unitPrice) {

		this.orderNo = orderNo;
		this.printed = printed;
		this.customerNo = customerNo;
		this.shipTo = shipTo;
		this.warehouse = warehouse;
		this.date = date;
		this.period = period;
		this.freight = freight;
		this.carrier = carrier;
		this.quantityShipped = quantityShipped;
		this.quantityOrdered = quantityOrdered;
		this.itemNo = itemNo;
		this.description = description;
		this.unitPrice = unitPrice;
		this.id = id;
	}

	public void setId(String id){
				this.id = id;
		}

		public String getId(){
				return this.id;
	}

	/**
	 * @return the carrier:String
	 */
	public String getCarrier() {
		return carrier;
	}

	/**
	 * @param carrier the carrier to set
	 */
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	/**
	 * @return the customerNo:String
	 */
	public String getCustomerNo() {
		return customerNo;
	}

	/**
	 * @param customerNo the customerNo to set
	 */
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	/**
	 * @return the date:String
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the description:String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the freight:String
	 */
	public String getFreight() {
		return freight;
	}

	/**
	 * @param freight the freight to set
	 */
	public void setFreight(String freight) {
		this.freight = freight;
	}

	/**
	 * @return the itemNo:String
	 */
	public String getItemNo() {
		return itemNo;
	}

	/**
	 * @param itemNo the itemNo to set
	 */
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	/**
	 * @return the orderNo:String
	 */
	public String getOrderNo() {
		return orderNo;
	}

	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * @return the period:int
	 */
	public int getPeriod() {
		return period;
	}

	/**
	 * @param period the period to set
	 */
	public void setPeriod(int period) {
		this.period = period;
	}

	/**
	 * @return the printed:String
	 */
	public String getPrinted() {
		return printed;
	}

	/**
	 * @param printed the printed to set
	 */
	public void setPrinted(String printed) {
		this.printed = printed;
	}

	/**
	 * @return the quantityOrdered:int
	 */
	public int getQuantityOrdered() {
		return quantityOrdered;
	}

	/**
	 * @param quantityOrdered the quantityOrdered to set
	 */
	public void setQuantityOrdered(int quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}

	/**
	 * @return the quantityShipped:int
	 */
	public int getQuantityShipped() {
		return quantityShipped;
	}

	/**
	 * @param quantityShipped the quantityShipped to set
	 */
	public void setQuantityShipped(int quantityShipped) {
		this.quantityShipped = quantityShipped;
	}

	/**
	 * @return the shipTo:String
	 */
	public String getShipTo() {
		return shipTo;
	}

	/**
	 * @param shipTo the shipTo to set
	 */
	public void setShipTo(String shipTo) {
		this.shipTo = shipTo;
	}

	/**
	 * @return the unitPrice:double
	 */
	public double getUnitPrice() {
		return unitPrice;
	}

	/**
	 * @param unitPrice the unitPrice to set
	 */
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	/**
	 * @return the warehouse:String
	 */
	public String getWarehouse() {
		return warehouse;
	}

	/**
	 * @param warehouse the warehouse to set
	 */
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}


}
