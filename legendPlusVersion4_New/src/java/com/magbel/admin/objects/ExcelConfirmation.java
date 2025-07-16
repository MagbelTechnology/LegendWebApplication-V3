// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   HtmlUtilily.java

package com.magbel.admin.objects;


public class ExcelConfirmation implements java.io.Serializable{

	private String id;
	private String chequeNo;
	private String CreationDate;
	private String IssueDescription;
	private String IssueSubject;
	private double amount;
	private String amountInWords;
	private String beneficiary;
	private String branch;
	private String status;
	private String userId;
	private String authourizedUser;
	private String confirmedUser;
	private String StatusCode;
	private String Assignee;
	private String deleteReason;
	
	public ExcelConfirmation()
	{
			
	}

	public ExcelConfirmation(String id,String chequeNo,String CreationDate,
						String IssueDescription, String IssueSubject,double amount,
						String amountInWords,String beneficiary,String branch,
						String status,String userId,String authourizedUser,
						String confirmedUser, String StatusCode, String Assignee,
						String deleteReason){

		setId(id);
		setChequeNo(chequeNo);
		setCreationDate(CreationDate);
		setIssueDescription(IssueDescription);
		setIssueSubject(IssueSubject);
		setAmount(amount);
		setAmountInWords(amountInWords);
		setBeneficiary(beneficiary);
		setStatus(status);
		setUserId(userId);
		setBranch(branch);
		setAuthourizedUser(authourizedUser);
		setConfirmedUser(confirmedUser);
		setStatusCode(StatusCode);
		setAssignee(Assignee);
		this.deleteReason = deleteReason;
	}

	public void setChequeNo(String chequeNo){
		this.chequeNo = chequeNo;
	}

	public void setId(String id){
		this.id = id;
	}

	public void setCreationDate(String CreationDate){
		this.CreationDate = CreationDate;
	}

	public void setIssueDescription(String IssueDescription){
		this.IssueDescription = IssueDescription;
	}

	public void setIssueSubject(String IssueSubject){
		this.IssueSubject = IssueSubject;
	}

	public void setAmount(double amount){
		this.amount = amount;
	}

	public void setAmountInWords(String amountInWords){
		this.amountInWords = amountInWords;
	}

	public void setBeneficiary(String beneficiary){
		this.beneficiary = beneficiary;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public void setBranch(String branch){
		this.branch = branch;
	}

	public void setAuthourizedUser(String authourizedUser){
		this.authourizedUser = authourizedUser;
	}

	public void setConfirmedUser(String confirmedUser){
		this.confirmedUser = confirmedUser;
	}

	public void setStatusCode(String StatusCode){
		this.StatusCode = StatusCode;
	}

	public void setAssignee(String Assignee){
		this.Assignee = Assignee;
	}

	public String getId() {
        return id;
    }

	 public String getChequeNo() {
        return chequeNo;
    }

	 public String getCreationDate() {
        return CreationDate;
    }

	 public String getIssueDescription() {
        return IssueDescription;
    }

	 public String getIssueSubject() {
        return IssueSubject;
    }

	 public double getAmount() {
        return amount;
    }

	 public String getAmountInWords() {
        return amountInWords;
    }
	 public String getBeneficiary() {
        return beneficiary;
    }

	 public String getBranch() {
        return branch;
    }

	 public String getStatus() {
        return status;
    }

	 public String getUserId() {
        return userId;
    }

	 public String getAuthourizedUser() {
        return authourizedUser;
    }

	 public String getConfirmedUser() {
        return confirmedUser;
    }

	 public String getStatusCode() {
        return StatusCode;
    }

    public String getAssignee(){
		return this.Assignee;
	}
    public String getDeleteReason(){
		return this.deleteReason;
	}
}
