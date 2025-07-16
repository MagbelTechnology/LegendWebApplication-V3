package com.magbel.legend.vao;

/**
 * <p>
 * Title: Company.java
 * </p>
 *
 * <p>
 * Description: Company
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 *
 * <p>
 * Company: Magbel Technologies LTD
 * </p>
 *
 * @author Lekan Matanmi.
 * @version 1.0
 */

public class RFID {

	    private String rfidTag ="";
	    private int  mtid =0;
	    private String  createDate ="";
	    private String location  ="";
	    private String  scannStatus ="";
	    private String  createDateTime ="";
	    private String  scannType="";
        private int userId = 0;
        private String groupid = "";
        private String description = "";
        private String processed = "";
	    private String  usedBy="";
        private int quantity = 0;   
        private String  itemCode="";
        private String  itemType="";
        private String  unitCode="";
        private int quantitybal = 0;  
        private int branchId = 0;  
        private int deptId = 0;  

    public RFID() {  
    }
    
   public RFID(int mtid,String createDate,String createDateTime,String location,String scannStatus) {
   this.mtid=mtid;
   this.createDate = createDate;
   this.createDateTime = createDateTime;
   this.scannStatus = scannStatus;
   this.location  = location;

    }

	public RFID(String rfidTag, int mtid,
			String createDate, String location, String scannStatus,
			String createDateTime, String scannType, int userId,String description,String groupid) {
		super();
		this.rfidTag = rfidTag;
		this.mtid = mtid;
		this.createDate = createDate;
		this.location = location;
		this.scannStatus = scannStatus;
		this.createDateTime = createDateTime;
		this.scannType = scannType;
        this.userId = userId;
        this.description = description;
        this.groupid = groupid;
	}

	public RFID(String rfidTag, int mtid,
			String createDate, String location, String scannStatus,
			String createDateTime, String scannType, int userId,String description,String groupid,
			String usedBy,int quantity) {
		super();
		this.rfidTag = rfidTag;
		this.mtid = mtid;
		this.createDate = createDate;
		this.location = location;
		this.scannStatus = scannStatus;
		this.createDateTime = createDateTime;
		this.scannType = scannType;
        this.userId = userId;
        this.description = description;
        this.groupid = groupid;
        this.usedBy = usedBy;
        this.quantity = quantity;
	}

	public RFID(String rfidTag, int mtid,
			String createDate, String location, String scannStatus,
			String createDateTime, String scannType, int userId,String description,String groupid,
			String usedBy,int quantity,int quantitybal) {
		super();
		this.rfidTag = rfidTag;
		this.mtid = mtid;
		this.createDate = createDate;
		this.location = location;
		this.scannStatus = scannStatus;
		this.createDateTime = createDateTime;
		this.scannType = scannType;
        this.userId = userId;
        this.description = description;
        this.groupid = groupid;
        this.usedBy = usedBy;
        this.quantity = quantity;
        this.quantitybal = quantitybal;
	}
        public RFID(String rfidTag, int mtid, String scannType, int userId) {
		super();
		this.rfidTag = rfidTag;
		this.mtid = mtid;
		this.scannType = scannType;
        this.userId = userId;
	}
      

	public String getRfidTag() {
		return rfidTag;
	}

	public void setRfidTag(String rfidTag) {
		this.rfidTag = rfidTag;
	}

	public int getMtid() {
		return mtid;
	}

	public void setMtid(int mtid) {
		this.mtid = mtid;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getScannStatus() {
		return scannStatus;
	}

	public void setScannStatus(String scannStatus) {
		this.scannStatus = scannStatus;
	}

	public String getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(String createDateTime) {
		this.createDateTime = createDateTime;
	}

	public String getScannType() {
		return scannType;
	}

	public void setIScannType(String scannType) {
		this.scannType = scannType;
	}

    /**
     * @return the userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}	

	public String getProcessed() {
		return processed;
	}

	public void setProcessed(String processed) {
		this.processed = processed;
	}

	public String getUsedBy() {
		return usedBy;
	}

	public void setUsedBy(String usedBy) {
		this.usedBy = usedBy;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}	

	public int getQuantitybal() {
		return quantitybal;
	}

	public void setQuantitybal(int quantitybal) {
		this.quantitybal = quantitybal;
	}	

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}	

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}	


	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}	

	
}
