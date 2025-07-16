package com.magbel.admin.objects;

public class NewIssueMailRecord { 
	private String id;

	private String category;
	
	private String subcategory;
	 
	private String assetId;
	
	private String priority;
	
	private String status;
	private String create_date;

	private String create_dateTime;
	
	private String userId;
	 
	private String complaintType;
	
	private String incidentMode;
	
	private String MailIssue;
	private String requesterId;

	private String requestDescription;
	
	private String requestSubject;
	 
	private String complaintId;
	
	private String companyCode;
	private String technician;
	private String notify_Email;
	
	public NewIssueMailRecord() {

	}     
	public NewIssueMailRecord(String id, String category,String subcategory,String assetId, String priority, String status,String create_date,String create_dateTime,String userId, String complaintType,String incidentMode,String MailIssue,String requesterId,String requestDescription,String requestSubject,String complaintId,String companyCode,String technician,String notify_Email) {

		super();
		this.id = id;
		this.category = category;
		this.subcategory = subcategory;
		this.assetId = assetId;
		this.priority = priority;
		this.status = status;
		this.create_date = create_date;
		this.create_dateTime = create_dateTime;
		this.userId = userId;
		this.complaintType = complaintType;
		this.incidentMode = incidentMode;
		this.MailIssue = MailIssue;			
		this.requesterId = requesterId;
		this.requestDescription = requestDescription;
		this.requestSubject = requestSubject;
		this.complaintId = complaintId;
		this.companyCode = companyCode;
		this.technician = technician;
		this.notify_Email = notify_Email;		
		
	} 

	public String getid() {
		return id;
	}

	public void setid(String id) {
		this.id = id;
	}
	public String getcategory() {
		return category;
	}

	public void setsender(String category) {
		this.category = category;
	}

	public String getsubcategory() {
		return subcategory;
	}

	public void setsubcategory(String subcategory) {
		this.subcategory = subcategory;
	}


	public String getassetId() {
		return assetId;
	}

	public void setassetId(String assetId) {
		this.assetId = assetId;
	}
    public String getpriority() {
		return priority;
	}

	public void setpriority(String priority) {
		this.priority = priority;
	}
	public String getstatus() {
		return status;
	}

	public void setstatus(String status) {
		this.status = status;
	}
		
	public String getcreate_date() {
		return create_date;
	}

	public void setcreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getcreate_dateTime() {
		return create_dateTime;
	}

	public void setcreate_dateTime(String create_dateTime) {
		this.create_dateTime = create_dateTime;
	}
	public String getuserId() {
		return userId;
	}

	public void setuserId(String userId) {
		this.userId = userId;
	}
	public String getcomplaintType() {
		return complaintType;
	}

	public void setcomplaintType(String complaintType) {
		this.complaintType = complaintType;
	}
	public String getincidentMode() {
		return incidentMode;
	}

	public void setincidentMode(String incidentMode) {
		this.incidentMode = incidentMode;
	}
	public String getMailIssue() {
		return MailIssue;
	}

	public void setMailIssue(String MailIssue) {
		this.MailIssue = MailIssue;
	}
	public String getrequesterId() {
		return requesterId;
	}

	public void setrequesterId(String requesterId) {
		this.requesterId = requesterId;
	}
	public String getrequestDescription() {
		return requestDescription;
	}

	public void setrequestDescription(String requestDescription) {
		this.requestDescription = requestDescription;
	}
	public String getrequestSubject() {
		return requestSubject;
	}

	public void setrequestSubject(String requestSubject) {
		this.requestSubject = requestSubject;
	}
	public String getcomplaintId() {
		return complaintId;
	}

	public void setcomplaintId(String complaintId) {
		this.complaintId = complaintId;
	}
	public String getcompanyCode() {
		return companyCode;
	}

	public void setcompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String gettechnician() {
		return technician;
	}

	public void settechnician(String technician) {
		this.technician = technician;
	}	
	public String getnotify_Email() {
		return notify_Email;
	}

	public void setnotify_Email(String notify_Email) {
		this.notify_Email = notify_Email;
	}		
	
}
