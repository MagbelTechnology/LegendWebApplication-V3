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
 * @author Ganiyu S.
 * @version 1.0
 */

public class ApprovalRemark {

	    private String assetId ="";
	    private int  supervisorID =0;
	    private String  DateRequisitioned ="";
	    private String Remark  ="";
	    private String  status ="";
	    private int ApprovalLevel  =0;
	    private String  RemarkDate ="";
	    private String  IPAddress="";
        private int transactionId = 0;
        private String tranType ="";

    public ApprovalRemark() {
    }

    public ApprovalRemark(int supervisor,int approvelevel,String sentDate,String approveDate,String remarks,String status) {
   this.supervisorID=supervisor;
   this.ApprovalLevel= approvelevel;
   this.DateRequisitioned = sentDate;
   this.RemarkDate = approveDate;
   this.status = status;
   this.Remark  = remarks;

    }

	public ApprovalRemark(String asset_id, int supervisorID,
			String dateRequisitioned, String remark, String status,
			int approvalLevel, String remarkDate, String address, int tranId,String tranType) {
		super();
		this.assetId = asset_id;
		this.supervisorID = supervisorID;
		this.DateRequisitioned = dateRequisitioned;
		this.Remark = remark;
		this.status = status;
		this.ApprovalLevel = approvalLevel;
		this.RemarkDate = remarkDate;
		this.IPAddress = address;
        this.transactionId = tranId;
        this.tranType = tranType;
	}

	public ApprovalRemark(String asset_id, int supervisorID,
			String dateRequisitioned, String remark, String status,
			int approvalLevel, String remarkDate, String address, int tranId) {
		super();
		this.assetId = asset_id;
		this.supervisorID = supervisorID;
		this.DateRequisitioned = dateRequisitioned;
		this.Remark = remark;
		this.status = status;
		this.ApprovalLevel = approvalLevel;
		this.RemarkDate = remarkDate;
		this.IPAddress = address;
                this.transactionId = tranId;
	}

        public ApprovalRemark(String asset_id, int supervisorID, String address, int tranId,String tranType) {
		super();
		this.assetId = asset_id;
		this.supervisorID = supervisorID;
		this.IPAddress = address;
                this.transactionId = tranId;
                this.tranType = tranType;
	}

        public ApprovalRemark(String asset_id, int supervisorID, int tranId) {
		super();
		this.assetId = asset_id;
		this.supervisorID = supervisorID;
		this.transactionId = tranId;
	}

      

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String id) {
		assetId = id;
	}

	public int getSupervisorID() {
		return supervisorID;
	}

	public void setSupervisorID(int supervisorID) {
		this.supervisorID = supervisorID;
	}

	public String getDateRequisitioned() {
		return DateRequisitioned;
	}

	public void setDateRequisitioned(String dateRequisitioned) {
		this.DateRequisitioned = dateRequisitioned;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getApprovalLevel() {
		return ApprovalLevel;
	}

	public void setApprovalLevel(int approvalLevel) {
		ApprovalLevel = approvalLevel;
	}

	public String getRemarkDate() {
		return RemarkDate;
	}

	public void setRemarkDate(String remarkDate) {
		RemarkDate = remarkDate;
	}

	public String getIPAddress() {
		return IPAddress;
	}

	public void setIPAddress(String address) {
		IPAddress = address;
	}

    /**
     * @return the transactionId
     */
    public int getTransactionId() {
        return transactionId;
    }

    /**
     * @param transactionId the transactionId to set
     */
    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * @return the tranType
     */
    public String getTranType() {
        return tranType;
    }

    /**
     * @param tranType the tranType to set
     */
    public void setTranType(String tranType) {
        this.tranType = tranType;
    }




}
