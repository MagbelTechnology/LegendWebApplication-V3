package com.magbel.admin.objects;

import com.magbel.admin.dao.*;

public class ProblemRecordItemize
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
        private String projectCode;
        private String complaint;
        private int recipientId;
        private String recipientEmail;
        private int senderId;
        private int responseRecipientId;
        private String priority;
        private String category;
        private String subCategory;
        private String complainType;
        private String incidentMode;
        private String responseMode;
        private String requesterName;
        private String requesterContactNo;
        private String requestSubject;
        private String requestDescription;
        private String assetId;
        private int id;
        private String createDate;
        private int statux;
        private int userID;
        private String technician;
         private String dueDate;
        
        
    public String getDueDate() {
			return dueDate;
		}

		public void setDueDate(String dueDate) {
			this.dueDate = dueDate;
		}
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getStatux() {
        return statux;
    }

    public void setStatux(int statux) {
        this.statux = statux;
    }
        
        

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
        private String creatTime;
        
        

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
        
        
    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getRequestDescription() {
        return requestDescription;
    }

    public void setRequestDescription(String requestDescription) {
        this.requestDescription = requestDescription;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getComplainType() {
        return complainType;
    }

    public void setComplainType(String complainType) {
        this.complainType = complainType;
    }

    public String getIncidentMode() {
        return incidentMode;
    }

    public void setIncidentMode(String incidentMode) {
        this.incidentMode = incidentMode;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getRequestSubject() {
        return requestSubject;
    }

    public void setRequestSubject(String requestSubject) {
        this.requestSubject = requestSubject;
    }

    public String getRequesterContactNo() {
        return requesterContactNo;
    }

    public void setRequesterContactNo(String requesterContactNo) {
        this.requesterContactNo = requesterContactNo;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getResponseMode() {
        return responseMode;
    }

    public void setResponseMode(String responseMode) {
        this.responseMode = responseMode;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }
        
        
        
        
        
        
        
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

    /**
     * @return the projectCode
     */
    public String getProjectCode() {
        return projectCode;
    }

    /**
     * @param projectCode the projectCode to set
     */
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    /**
     * @return the complaint
     */
    public String getComplaint() {
        return complaint;
    }

    /**
     * @param complaint the complaint to set
     */
    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    /**
     * @return the recipientId
     */
    public int getRecipientId() {
        return recipientId;
    }

    /**
     * @param recipientId the recipientId to set
     */
    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }

    /**
     * @return the recipientEmail
     */
    public String getRecipientEmail() {
        return recipientEmail;
    }

    /**
     * @param recipientEmail the recipientEmail to set
     */
    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    /**
     * @return the senderId
     */
    public int getSenderId() {
        return senderId;
    }

    /**
     * @param senderId the senderId to set
     */
    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    /**
     * @return the responseRecipientId
     */
    public int getResponseRecipientId() {
        return responseRecipientId;
    }

    /**
     * @param responseRecipientId the responseRecipientId to set
     */
    public void setResponseRecipientId(int responseRecipientId) {
        this.responseRecipientId = responseRecipientId;
    }

    /**
     * @return the technician
     */
    public String getTechnician() {
        return technician;
    }

    /**
     * @param technician the technician to set
     */
    public void setTechnician(String technician) {
        this.technician = technician;
    }
	
	
}
