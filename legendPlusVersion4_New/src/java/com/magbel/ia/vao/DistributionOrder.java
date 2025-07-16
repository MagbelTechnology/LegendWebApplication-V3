package com.magbel.ia.vao;


public class DistributionOrder 
{

	private String orderNo;
	private String  posted;
	private String  status;
	private String  customerNo;
	private String  po;
	private String transDate;
	private String 	shipDate;
	private String 	freight;
	private String 	carrier;
	private String 	description;
	private String id;
    private int userId;
    private String supervisor;
    private String remark;
    private String remarkDate;
    private int quantity;
    private int quantitybalance;
    private double unitPrice;
    private String warehouseCode;
    private String reqnDescription;
    private String itemRequested;
    private double amount;
    private String unitCode;
    private String itemType;
    private String warehouse;
    private String measuringCode;
    
    public String getSupervisor() {
		return supervisor;
	}


	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getRemarkDate() {
		return remarkDate;
	}


	public void setRemarkDate(String remarkDate) {
		this.remarkDate = remarkDate;
	}


	public String getReqnID() {
		return reqnID;
	}


	public void setReqnID(String reqnID) {
		this.reqnID = reqnID;
	}


	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getQuantitybalance() {
		return quantitybalance;
	}

	public void setQuantitybalance(int quantitybalance) {
		this.quantitybalance = quantitybalance;
	}	
	
	
	private String reqPersIdentity;
    private String approveOfficer;
    private String projectCode;
    private String reqnID;

	/**
	 *
	 */
	public DistributionOrder() {

	}

    public DistributionOrder(String id, String orderNo, String description, String posted, String status, String customerNo, String po, 
            String transDate, String shipDate, String freight, String carrier, int userId, String reqPersIdentity, String approveOfficer, 
            String projectCode)
    {
        this.orderNo = orderNo;
        this.description = description;
        this.posted = posted;
        this.status = status;
        this.customerNo = customerNo;
        this.po = po;
        this.transDate = transDate;
        this.shipDate = shipDate;
        this.freight = freight;
        this.carrier = carrier;
        this.id = id;
        this.userId = userId;
        this.reqPersIdentity = reqPersIdentity;
        this.approveOfficer = approveOfficer;
        this.projectCode = projectCode;
    }

	
	public DistributionOrder(String id,String orderNo,String description,String posted, String status,
                          String customerNo, String po,String transDate, String shipDate, 
                          String freight, String carrier,int userId,String reqPersIdentity,
                          String approveOfficer,String projectCode, int quantity) {
	
         this.orderNo = orderNo;
         this.description = description;
		 this.posted = posted;
		 this.status = status;
		 this.customerNo = customerNo;
		 this.po = po;
		 this.transDate = transDate;
		 this.shipDate = shipDate;
		 this.freight = freight;
		 this.carrier = carrier;
		 this.id = id;
         this.userId = userId;
         this.reqPersIdentity = reqPersIdentity;
         this.approveOfficer = approveOfficer;
         this.projectCode = projectCode;
         this.quantity = quantity;
                
	}


    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setPosted(String posted) {
        this.posted = posted;
    }

    public String getPosted() {
        return posted;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public String getPo() {
        return po;
    }

    
    public void setShipDate(String shipDate) {
        this.shipDate = shipDate;
    }

    public String getShipDate() {
        return shipDate;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getFreight() {
        return freight;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setReqPersIdentity(String reqPersIdentity) {
        this.reqPersIdentity = reqPersIdentity;
    }

    public String getReqPersIdentity() {
        return reqPersIdentity;
    }

    public void setApproveOfficer(String approveOfficer) {
        this.approveOfficer = approveOfficer;
    }

    public String getApproveOfficer() {
        return approveOfficer;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }  

    public void setMeasuringCode(String measuringCode) {
        this.measuringCode = measuringCode;
    }

    public String getMeasuringCode() {
        return measuringCode;
    }  

    public void setReqnDescription(String reqnDescription) {
        this.reqnDescription = reqnDescription;
    }

    public String getReqnDescription() {
        return reqnDescription;
    }   

    public void setItemRequested(String itemRequested) {
        this.itemRequested = itemRequested;
    }

    public String getItemRequested() {
        return itemRequested;
    }     

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemType() {
        return itemType;
    }  

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getWarehouse() {
        return warehouse;
    }  
        
    public double getAmount()
    {
        return amount;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }

	public void setUnitCode(String unitCode){
	this.unitCode=unitCode;
	} 
	
	public String getUnitCode(){
	return unitCode;
	}	    
}
