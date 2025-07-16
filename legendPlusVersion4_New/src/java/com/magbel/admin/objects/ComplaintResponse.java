/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magbel.admin.objects;

import com.magbel.admin.dao.*;
/**
 *
 * @author Developer - Vickie
 */
public class ComplaintResponse {

    private int newRecipient;
    private int newReturnTo;
    private int remindTimeValue;
    private int returnTimeValue;
    private int userId;
    private int complaintNumber;
    private String complaintId;
    private String actionOnSelf;
    private String remindTimeMode;
    private String returnTimeMode;
    private String returnDate;
    private String actiontoPerform;
    private String newResponse;
    private String sessionId;
    private String IssueId;
    private int id;
    private String closeActivateReason;
    private String status;
    private String returnTime;
    private int statux;
    private String servicesAffected;
    private String FileName;
    
	 private String itemType;
	 private String itemRequested;
	 private String requestingBranch;
	 private String no_Of_Items;
	 private String requestingDept;
	 private String remark;
	 private String supervisor;
	 private String imageId;
	 private String ReqnID;
	 private String requestingSection;
	 private String company_code;
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
        private String createDate;
        private String CloseDate;
        private int userID;
        private String technician;
         private String dueDate;
         private String Solution_id;
         private String Solution_Title;
         private String Solution_Topic;
         private String Solution_Content;
         private String Solution_Keywords;
         private String Solution_Comments;
         private String Task_Content;
         private String ScheduleStarteDate;
         private String ScheduleEndDate;
         private String ActualStartDate;
         private String ActualEndDate;
         private String EmailMeBefore;         
         private String Task_id;
         private String Task_Title;
         private String Task_Topic;
         private String Task_Comments;
         private String Announce_id;
         private String Announce_Title;
         private String Announce_Content;
         private String Send_Mail;
         private String Email_To;
         private String Email_Copy;
         private String returncode;
         private String RestrictAnnouncement;
         private int Slaid;
    
    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }
    
    
    
    public ComplaintResponse() {
    }

    /**
     * @return the newRecipient
     */
    public int getNewRecipient() {
        return newRecipient;
    }

    /**
     * @param newRecipient the newRecipient to set
     */
    public void setNewRecipient(int newRecipient) {
        this.newRecipient = newRecipient;
    }
    /**
     * @return the newReturnTo
     */
    public int getNewReturnTo() {
        return newReturnTo;
    }

    /**
     * @param newReturnTo the newReturnTo to set
     */
    public void setNewReturnTo(int newReturnTo) {
        this.newReturnTo = newReturnTo;
    }

    /**
     * @return the remindTimeValue
     */
    public int getRemindTimeValue() {
        return remindTimeValue;
    }

    /**
     * @param remindTimeValue the remindTimeValue to set
     */
    public void setRemindTimeValue(int remindTimeValue) {
        this.remindTimeValue = remindTimeValue;
    }

    /**
     * @return the returnTimeValue
     */
    public int getReturnTimeValue() {
        return returnTimeValue;
    }

    /**
     * @param returnTimeValue the returnTimeValue to set
     */
    public void setReturnTimeValue(int returnTimeValue) {
        this.returnTimeValue = returnTimeValue;
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

    /**
     * @return the complaintNumber
     */
    public int getComplaintNumber() {
        return complaintNumber;
    }

    /**
     * @param complaintNumber the complaintNumber to set
     */
    public void setComplaintNumber(int complaintNumber) {
        this.complaintNumber = complaintNumber;
    }

    /**
     * @return the complaintId
     */
    public String getComplaintId() {
        return complaintId;
    }

    /**
     * @param complaintId the complaintId to set
     */
    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    /**
     * @return the actionOnSelf
     */
    public String getActionOnSelf() {
        return actionOnSelf;
    }

    /**
     * @param actionOnSelf the actionOnSelf to set
     */
    public void setActionOnSelf(String actionOnSelf) {
        this.actionOnSelf = actionOnSelf;
    }

    /**
     * @return the remindTimeMode
     */
    public String getRemindTimeMode() {
        return remindTimeMode;
    }

    /**
     * @param remindTimeMode the remindTimeMode to set
     */
    public void setRemindTimeMode(String remindTimeMode) {
        this.remindTimeMode = remindTimeMode;
    }

    /**
     * @return the returnTimeMode
     */
    public String getReturnTimeMode() {
        return returnTimeMode;
    }

    /**
     * @param returnTimeMode the returnTimeMode to set
     */
    public void setReturnTimeMode(String returnTimeMode) {
        this.returnTimeMode = returnTimeMode;
    }

    /**
     * @return the returnDate
     */
    public String getReturnDate() {
        return returnDate;
    }

    /**
     * @param returnDate the returnDate to set
     */
    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    /**
     * @return the actiontoPerform
     */
    public String getActiontoPerform() {
        return actiontoPerform;
    }

    /**
     * @param actiontoPerform the actiontoPerform to set
     */
    public void setActiontoPerform(String actiontoPerform) {
        this.actiontoPerform = actiontoPerform;
    }

    /**
     * @return the newResponse
     */
    public String getNewResponse() {
        return newResponse;
    }

    /**
     * @param newResponse the newResponse to set
     */
    public void setNewResponse(String newResponse) {
        this.newResponse = newResponse;
    }
    /**
     * @return the sessionId
     */
    public String getsessionId() {
        return sessionId;
    }
    
    /**
     * @param sessionId the sessionId to set
     */
    public void setsessionId(String sessionId) {
        this.sessionId = sessionId;
    }   
    /**
     * @return the IssueId
     */
    public String getIssueId() {
        return IssueId;
    }
    
    /**
     * @param IssueId the IssueId to set
     */
    public void setIssueId(String IssueId) {
        this.IssueId = IssueId;
    }      
    
    /**
     * @return the sessionId
     */
    public String getFileName() {
        return FileName;
    }
    
    /**
     * @param FileName the FileName to set
     */
    public void setFileName(String FileName) {
        this.FileName = FileName;
    }    
            
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the closeActivateReason
     */
    public String getCloseActivateReason() {
        return closeActivateReason;
    }

    /**
     * @param closeActivateReason the closeActivateReason to set
     */
    public void setCloseActivateReason(String closeActivateReason) {
        this.closeActivateReason = closeActivateReason;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the servicesAffected
     */
    public String getservicesAffected() {
        return servicesAffected;
    }

    /**
     * @param servicesAffected the servicesAffected to set
     */
    public void setservicesAffected(String servicesAffected) {
        this.servicesAffected = servicesAffected;
    }

    
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
    
    public String getCloseDate() {
        return CloseDate;
    }

    public void setCloseDate(String CloseDate) {
        this.CloseDate = CloseDate;
    }    
        private String creatTime;
               
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
    
    public String getSolution_id() {
		return Solution_id;
	}

	public void setSolution_id(String Solution_id) {
		this.Solution_id = Solution_id;
	}
	
	public String getSolution_Title() {
		return Solution_Title;
	}

	public void setSolution_Title(String Solution_Title) {
		this.Solution_Title = Solution_Title;
	}

	public String getSolution_Topic() {
		return Solution_Topic;
	}

	public void setSolution_Topic(String Solution_Topic) {
		this.Solution_Topic = Solution_Topic;
	}

	public String getSolution_Content() {
		return Solution_Content;
	}

	public void setSolution_Content(String Solution_Content) {
		this.Solution_Content = Solution_Content;
	}

	public String getSolution_Keywords() {
		return Solution_Keywords;
	}

	public void setSolution_Keywords(String Solution_Keywords) {
		this.Solution_Keywords = Solution_Keywords;
	}

	public String getSolution_Comments() {
		return Solution_Comments;
	}

	public void setSolution_Comments(String Solution_Comments) {
		this.Solution_Comments = Solution_Comments;
	}
    
    public String getScheduleStarteDate() {
			return ScheduleStarteDate;
		}

		public void setScheduleStarteDate(String ScheduleStarteDate) {
			this.ScheduleStarteDate = ScheduleStarteDate;
		}
	    public String getScheduleEndDate() {
			return ScheduleEndDate;
		}

		public void setScheduleEndDate(String ScheduleEndDate) {
			this.ScheduleEndDate = ScheduleEndDate;
		}		
		
	    public String getActualStartDate() {
			return ActualStartDate;
		}

		public void setActualStartDate(String ActualStartDate) {
			this.ActualStartDate = ActualStartDate;
		}		
		
	    public String getActualEndDatee() {
			return ActualEndDate;
		}

		public void setActualEndDate(String ActualEndDate) {
			this.ActualEndDate = ActualEndDate;
		}		
		public String getEmailMeBefore() {
			return EmailMeBefore;
		}

		public void setEmailMeBefore(String EmailMeBefore) {
			this.EmailMeBefore = EmailMeBefore;
		}
		
	    
	    public String getTask_id() {
			return Task_id;
		}

		public void setTask_id(String Task_id) {
			this.Task_id = Task_id;
		}
	
		public String getTask_Title() {
			return Task_Title;
		}

		public void setTask_Title(String Task_Title) {
			this.Task_Title = Task_Title;
		}

		public String getTask_Topic() {
			return Task_Topic;
		}

		public void setTask_Topic(String Task_Topic) {
			this.Task_Topic = Task_Topic;
		}
		public String getTask_Comments() {
			return Task_Comments;
		}

		public void setTask_Comments(String Task_Comments) {
			this.Task_Comments = Task_Comments;
		}
		public String getTask_Content() {
			return Task_Content;
		}

		public void setTask_Content(String Task_Content) {
			this.Task_Content = Task_Content;
		}
		public String getAnnounce_id() {
			return Announce_id;
		}

		public void setAnnounce_id(String Announce_id) {
			this.Announce_id = Announce_id;
		}
		public String getAnnounce_Title() {
			return Announce_Title;
		}

		public void setAnnounce_Title(String Announce_Title) {
			this.Announce_Title = Announce_Title;
		}
		public String getAnnounce_Content() {
			return Announce_Content;
		}

		public void setAnnounce_Content(String Announce_Content) {
			this.Announce_Content = Announce_Content;
		}
		public String getSend_Mail() {
			return Send_Mail;
		}

		public void setSend_Mail(String Send_Mail) {
	        if (Send_Mail == null || Send_Mail == "") {
	            this.Send_Mail = "N";
	        } else {
	            this.Send_Mail = Send_Mail;
	        }			
		}
		public String getEmail_To() {
			return Email_To;
		}

		public void setEmail_To(String Email_To) {
			this.Email_To = Email_To;
		}
		public String getEmail_Copy() {
			return Email_Copy;
		}

		public void setEmail_Copy(String Email_Copy) {
			this.Email_Copy = Email_Copy;
		}
		public String getreturncode() {
			return returncode;
		}

		public void setreturncode(String returncode) {
			this.returncode = returncode;
		}	
	    /**
	     * @return the Slaid
	     */
	    public int getSlaid() {
	        return Slaid;
	    }

	    /**
	     * @param userId the Slaid to set
	     */
	    public void setSlaid(int Slaid) {
	        this.Slaid = Slaid;
	    }
		public void setRestrictAnnouncement(String RestrictAnnouncement) {
	        if (RestrictAnnouncement == null || RestrictAnnouncement == "") {
	            this.RestrictAnnouncement = "N";
	        } else {
	            this.RestrictAnnouncement = RestrictAnnouncement;
	        }			
		}
		public String getRestrictAnnouncement() {
			return RestrictAnnouncement;
		}
		
		
}
    
    

