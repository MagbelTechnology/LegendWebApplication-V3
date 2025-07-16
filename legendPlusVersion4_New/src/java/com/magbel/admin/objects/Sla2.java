package com.magbel.admin.objects;

public class Sla2 { 
	private String ReqnID;

	private String SLAId;
	
	private String sla_name;
	
	private String Priority;
    private String requestSubject;
    private String requestDescription;
    private String status;
    private String Technician;
	private String Dept_Code;
	private String Cat_Code;
	private String ComplaintType;
    private String ResolveDue_Date;
    private String ResponseDue_Date;
    private String Create_Date;
    private String Create_Time;
    private String mailstatus;
	public Sla2() { 

	}     
  
	public Sla2(String ReqnID, String Priority,String SLAId, String requestSubject,String requestDescription, String status, String Technician, String Dept_Code, String Cat_Code,String ComplaintType, String ResolveDue_Date, String ResponseDue_Date,String Create_Date,String mailstatus,String Create_Time) {
		super();
		this.ReqnID = ReqnID;
		this.SLAId = SLAId;
		this.Priority = Priority;
		//this.sla_name = sla_name;
		this.requestSubject = requestSubject;
		this.requestDescription = requestDescription;
		this.status = status;
		this.Technician = Technician;
		this.Dept_Code = Dept_Code;
		this.Cat_Code = Cat_Code;
		this.ComplaintType = ComplaintType;
		this.ResolveDue_Date = ResolveDue_Date;
		this.ResponseDue_Date = ResponseDue_Date;
		this.Create_Date = Create_Date;
		this.Create_Time = Create_Time;
		this.mailstatus = mailstatus; 
	} 
 
	public String getReqnID() {
		return ReqnID;
	}

	public void setReqnID(String ReqnID) {
		this.ReqnID = ReqnID;
	}
	public String getSLAId() {
		return SLAId;
	}

	public void setSLAId(String SLAId) {
		this.SLAId = SLAId;
	}

	public String getSla_name() {
		return sla_name;
	}

	public void setSla_name(String sla_name) {
		this.sla_name = sla_name;
	}


	public String getPriority() {
		return Priority;
	}

	public void setPriority(String Priority) {
		this.Priority = Priority;
	}
		
	public String getrequestSubject() {
		return requestSubject;
	}

	public void setrequestSubject(String requestSubject) {
		this.requestSubject = requestSubject;
	}

	public String getrequestDescription() {
		return requestDescription;
	}

	public void setrequestDescription(String requestDescription) {
		this.requestDescription = requestDescription;
	}

	public String getstatus() {
		return status;
	}

	public void setstatus(String status) {
		this.status = status;
	}
	public String getTechnician() {
		return Technician;
	}

	public void setTechnician(String Technician) {
		this.Technician = Technician;
	}
	public String getDept_Code() {
		return Dept_Code;
	}

	public void setDept_Code(String Dept_Code) {
		this.Dept_Code = Dept_Code;
	}
	public String getCat_Code() {
		return Cat_Code;
	}

	public void setCat_Code(String Cat_Code) {
		this.Cat_Code = Cat_Code;
	}
	public String getComplaintType() {
		return ComplaintType;
	}

	public void setComplaintType(String ComplaintType) {
		this.ComplaintType = ComplaintType;
	}
    public String getResolveDue_Date() {
		return ResolveDue_Date;
	}

	public void setResolveDue_Date(String ResolveDue_Date) {
		this.ResolveDue_Date = ResolveDue_Date;
	}		
	
    public String getResponseDue_Date() {
		return ResponseDue_Date;
	}

	public void setResponseDue_Date(String ResponseDue_Date) {
		this.ResponseDue_Date = ResponseDue_Date;
	}
    public String getCreate_Date() {
		return Create_Date;
	}

	public void setCreate_Date(String Create_Date) {
		this.Create_Date = Create_Date;
	}
    public String getCreate_Time() {
		return Create_Time;
	} 

	public void setCreate_Time(String Create_Time) {
		this.Create_Time = Create_Time;
	}
    public String getmailstatus() {
		return mailstatus;
	}

	public void setmailstatus(String mailstatus) {
		this.mailstatus = mailstatus;
	}	
	
}
