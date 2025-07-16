package com.magbel.ia.vao;


public class PurchaseOrder {

	private String id;
	private String orderNo;

	private String vendorCode;

	private String printed;

	private String status;

	private double amount;

	private String transDate;

	private String dateReceived;

	private String orderedBy;

	private String warehouse;

	private String comment;

	private String carrier;

	private String freight;

	private int quantity;

	private String vendorItemNo;

	private String description;

	private double unitCost;

	private double amount_paid;

	private String overrideDescription;

	private int quantityReceived;

	private int quantityBO;

	private String completed;
        private String advancePymtOpt;
        private String projectCode;

	/**
	 * <p>Description: generic constructor</p>
	 *
	 */
	public PurchaseOrder() {

	}

	/**
	 * <p>Description: class constructor using fields</p>
	 *
	 */
	public PurchaseOrder(String id, String orderNo, String vendorCode, String printed,
			String status, double amount, String transDate, String dateReceived,
			String orderedBy, String warehouse, String comment, String carrier,
			String freight, int quantity, String vendorItemNo,
			String description, double unitCost, double amount_paid,
			String advancePymtOpt, int quantityReceived,
			int quantityBO, String completed,String projectCode) {
		super();

		this.id = id;
		this.orderNo = orderNo;
		this.vendorCode = vendorCode;
		this.printed = printed;
		this.status = status;
		this.amount = amount;
		this.transDate = transDate;
		this.dateReceived = dateReceived;
		this.orderedBy = orderedBy;
		this.warehouse = warehouse;
		this.comment = comment;
		this.carrier = carrier;
		this.freight = freight;
		this.quantity = quantity;
		this.vendorItemNo = vendorItemNo;
		this.description = description;
		this.unitCost = unitCost;
		this.amount_paid = amount_paid;
		this.advancePymtOpt = advancePymtOpt;
		this.quantityReceived = quantityReceived;
		this.quantityBO = quantityBO;
		this.completed = completed;
                this.projectCode = projectCode;
	}

	public void setId(String id){
			this.id = id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getAmount_paid() {
		return amount_paid;
	}

	public void setAmount_paid(double amount_paid) {
		this.amount_paid = amount_paid;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCompleted() {
		return completed;
	}

	public void setCompleted(String completed) {
		this.completed = completed;
	}

	public String getDateReceived() {
		return dateReceived;
	}

	public void setDateReceived(String dateReceived) {
		this.dateReceived = dateReceived;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFreight() {
		return freight;
	}

	public void setFreight(String freight) {
		this.freight = freight;
	}

	public String getOrderedBy() {
		return orderedBy;
	}

	public void setOrderedBy(String orderedBy) {
		this.orderedBy = orderedBy;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	

	public String getPrinted() {
		return printed;
	}

	public void setPrinted(String printed) {
		this.printed = printed;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getQuantityBO() {
		return quantityBO;
	}

	public void setQuantityBO(int quantityBO) {
		this.quantityBO = quantityBO;
	}

	public int getQuantityReceived() {
		return quantityReceived;
	}

	public void setQuantityReceived(int quantityReceived) {
		this.quantityReceived = quantityReceived;
	}

	public String getId(){
			return this.id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

    public void setAdvancePymtOpt(String advancePymtOpt) {
        this.advancePymtOpt = advancePymtOpt;
    }

    public String getAdvancePymtOpt() {
        return advancePymtOpt;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectCode() {
        return projectCode;
    }
}
