package com.magbel.ia.vao;

public class Requisition 
{
	String itemType;
	String itemRequested;
	String requestingBranch;
	String no_Of_Items;
	String requestingDept;
	String remark;
	String supervisor;
	String imageId;
	String userId;
	String ReqnID;
	String requestingSection;
	String company_code;
	String status;
	String ReqnUserId;
	int approvLevel;
	int aprovLevelLimit;
	String remarkDate;
	String requisitionDate;
	String isImage;
	String distributedQty;
	String projCode;
	String unitCode;
	String returnedCategory;
	String reqnType;
	String measuringCode;
	String returnedStock;
	String category;
	String quantity;
	String stockReturned;
	String returnedOrderNo;
	
	public Requisition(){
		
	}
	public Requisition(String ReqnID,String requestingBranch,String requestingDept, String requestingSection,
                          String remark, String ReqnUserId,String distributedQty, String returnedCategory, 
                          String reqnType, String measuringCode,String userId,String returnedStock,String category,
                          String projCode, String quantity,String itemType,String itemRequested, String stockReturned) {
	
         this.ReqnID = ReqnID;
         this.requestingBranch = requestingBranch;
		 this.requestingDept = requestingDept;
		 this.requestingSection = requestingSection;
		 this.remark = remark;
		 this.ReqnUserId = ReqnUserId;
		 this.distributedQty = distributedQty;
		 this.returnedCategory = returnedCategory;
		 this.reqnType = reqnType;
		 this.measuringCode = measuringCode;
         this.userId = userId;
         this.returnedStock = returnedStock;
         this.category = category;
         this.projCode = projCode;
         this.quantity = quantity;
         this.itemType = itemType;
         this.itemRequested = itemRequested;
         this.stockReturned = stockReturned;
                
	}
	
	public String getProjCode() {
		return projCode;
	}
	public void setProjCode(String projCode) {
		this.projCode = projCode;
	}
	public String getDistributedQty() {
		return distributedQty;
	}
	public void setDistributedQty(String distributedQty) {
		this.distributedQty = distributedQty;
	}
	public String getIsImage() {
		return isImage;
	}
	public void setIsImage(String isImage) {
		this.isImage = isImage;
	}
	public String getRequisitionDate() {
		return requisitionDate;
	}
	public void setRequisitionDate(String requisitionDate) {
		this.requisitionDate = requisitionDate;
	}
	public String getRemarkDate() {
		return remarkDate;
	}
	public void setRemarkDate(String remarkDate) {
		this.remarkDate = remarkDate;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getItemRequested() {
		return itemRequested;
	}
	public void setItemRequested(String itemRequested) {
		this.itemRequested = itemRequested;
	}
	public String getRequestingBranch() {
		return requestingBranch;
	}
	public void setRequestingBranch(String requestingBranch) {
		this.requestingBranch = requestingBranch;
	}
	public String getNo_Of_Items() {
		return no_Of_Items;
	}
	public void setNo_Of_Items(String no_Of_Items) {
		this.no_Of_Items = no_Of_Items;
	}
	public String getRequestingDept() {
		return requestingDept;
	}
	public void setRequestingDept(String requestingDept) {
		this.requestingDept = requestingDept;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSupervisor() {
		return supervisor;
	}
	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getReqnID() {
		return ReqnID;
	}
	public void setReqnID(String reqnID) {
		ReqnID = reqnID;
	}
	public String getRequestingSection() {
		return requestingSection;
	}
	public void setRequestingSection(String requestingSection) {
		this.requestingSection = requestingSection;
	}
	public String getCompany_code() {
		return company_code;
	}
	public void setCompany_code(String company_code) {
		this.company_code = company_code;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getReqnUserId() {
		return ReqnUserId;
	}
	public void setReqnUserId(String reqnUserId) {
		ReqnUserId = reqnUserId;
	}
	public int getApprovLevel() {
		return approvLevel;
	}
	public void setApprovLevel(int approvLevel) {
		this.approvLevel = approvLevel;
	}
	public int getAprovLevelLimit() {
		return aprovLevelLimit;
	}
	public void setAprovLevelLimit(int aprovLevelLimit) {
		this.aprovLevelLimit = aprovLevelLimit;
	}

	public void setUnitCode(String unitCode){
	this.unitCode=unitCode;
	} 
	
	public String getUnitCode(){
	return unitCode;
	}	

	public void setReturnedCategory(String returnedCategory){
	this.returnedCategory=returnedCategory;
	} 
	
	public String getReturnedCategory(){
	return returnedCategory;
	}	

	public void setReqnType(String reqnType){
	this.reqnType=reqnType;
	} 
	
	public String getReqnType(){
	return reqnType;
	}	

	public void setMeasuringCode(String measuringCode){
	this.measuringCode=measuringCode;
	} 
	
	public String getMeasuringCode(){
	return measuringCode;
	}	

	public void setReturnedStock(String returnedStock){
	this.returnedStock=returnedStock;
	} 
	
	public String getReturnedStock(){
	return returnedStock;
	}	

	public void setCategory(String category){
	this.category=category;
	} 
	
	public String getCategory(){
	return category;
	}	

	public void setQuantity(String quantity){
	this.quantity=quantity;
	} 
	
	public String getQuantity(){
	return quantity;
	}

	public void setStockReturned(String stockReturned){
	this.stockReturned=stockReturned;
	} 
	
	public String getStockReturned(){
	return stockReturned;
	}	

	public void setReturnedOrderNo(String returnedOrderNo){
	this.returnedOrderNo=returnedOrderNo;
	} 
	
	public String getReturnedOrderNo(){
	return returnedOrderNo;
	}	
	
}
