package com.magbel.admin.objects;

public class Sla { 
	private String sla_ID;

	private String sla_name;

	private String sla_description;
	private String DeptCode; 
	
	private String CatCode;

	private String userId;

	private String createDate;
	private String Status;
	public Sla() {

	}   

	public Sla(String sla_ID, String DeptCode, String sla_name, String sla_description,
			String userId, String createDate, String Status) {
		super();
		this.sla_ID = sla_ID;
		this.DeptCode = DeptCode;
		this.sla_name = sla_name;
		this.sla_description = sla_description;
		this.userId = userId;
		this.createDate = createDate;
		this.Status = Status;
	}
 
	public String getSla_ID() {
		return sla_ID;
	}

	public void setSla_ID(String sla_ID) {
		this.sla_ID = sla_ID;
	}

	public String getSla_name() {
		return sla_name;
	}

	public void setSla_name(String sla_name) {
		this.sla_name = sla_name;
	}

	public String getSla_description() {
		return sla_description;
	}

	public void setSla_description(String sla_description) {
		this.sla_description = sla_description;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCreateDate() {
		return createDate;
	}
   
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getDeptCode() {
		return DeptCode;
	}

	public void setDeptCode(String DeptCode) {
		this.DeptCode = DeptCode;
	}
	public String getCatCode() {
		return CatCode;
	}

	public void setCatCode(String CatCode) {
		this.CatCode = CatCode;
	}
	public String getStatus() {
		return Status;
	}

	public void setStatus(String Status) {
		this.Status = Status;
	}
	
}
