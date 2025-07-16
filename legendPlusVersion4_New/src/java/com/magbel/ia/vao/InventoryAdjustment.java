package com.magbel.ia.vao;

public class InventoryAdjustment {

	private String id;
	private String code;
	private String itemNo;
	private String transDate;
	private String period;
	private String warehouse;
	private String description;
	private int quantity;
    private int userId;
    private String posted; 
    private String adjustOpt;
	private String reason;
	
	public InventoryAdjustment() {

	}
	public InventoryAdjustment(String id,String itemNo,String transDate,String period,
			           String warehouse,String description,int quantity,int userId,
                                   String posted,String adjustOpt) {

	this.id = id;
	this.itemNo = itemNo;
	this.transDate = transDate;
	this.period = period;
	this.warehouse = warehouse;
	this.description = description;
	this.quantity = quantity;
        this.posted = posted;
        this.adjustOpt = adjustOpt;

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	
	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}
	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setPosted(String posted) {
        this.posted = posted;
    }

    public String getPosted() {
        return posted;
    }

    public void setAdjustOpt(String adjustOpt) {
        this.adjustOpt = adjustOpt;
    }

    public String getAdjustOpt() {
        return adjustOpt;
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

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
    
}


