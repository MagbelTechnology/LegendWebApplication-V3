package magma.net.vao;

public class ProcureRequisition
{
	 private String itemType;
	 private String itemRequested;
	 private String requestingBranch;
	 private String no_Of_Items;
	 private String requestingDept;
	 private String remark;
	 private String supervisor;
	 private String imageId;
	 private String userId;
	 private String ReqnID;
	 private String requestingSection;
	 private String company_code;
	 private String status;
	 private String ReqnUserId;
	 private int approvLevel;
	 private int aprovLevelLimit;
	 private String remarkDate;
	 private String requisitionDate;
	 private String isImage;
	 private String tranID;
	private int quantity;
        private String requistionTitle;
        private int requisitionNum;
        private String ipAddress;

	public String getTranID() {
		return tranID;
	}
	public void setTranID(String tranID) {
		this.tranID = tranID;
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

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the requistionTitle
     */
    public String getRequistionTitle() {
        return requistionTitle;
    }

    /**
     * @param requistionTitle the requistionTitle to set
     */
    public void setRequistionTitle(String requistionTitle) {
        this.requistionTitle = requistionTitle;
    }

    /**
     * @return the requisitionNum
     */
    public int getRequisitionNum() {
        return requisitionNum;
    }

    /**
     * @param requisitionNum the requisitionNum to set
     */
    public void setRequisitionNum(int requisitionNum) {
        this.requisitionNum = requisitionNum;
    }

    /**
     * @return the ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @param ipAddress the ipAddress to set
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
	
	
}
