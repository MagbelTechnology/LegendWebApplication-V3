package com.magbel.admin.objects;

public class Category_Definition {
	private String CATEGORY_ID;
	private String CATEGORY_NAME;
	private String CATEGORY_DESCRIPTION;
	private String IS_DELETED;
	private String tech_id;
	private String user_id;
	private String Status;
	private String create_date;
	public Category_Definition() {
		super();
	}
	public Category_Definition(String category_id, String category_name,
			String category_description, String status) {
		super();
		CATEGORY_ID = category_id;
		CATEGORY_NAME = category_name;
		CATEGORY_DESCRIPTION = category_description;
		Status = status;
	} 
	
	public Category_Definition(String category_id, String category_name,
			String category_description, String status,
			String user_id, String create_date) {
		super();
		CATEGORY_ID = category_id;
		CATEGORY_NAME = category_name;
		CATEGORY_DESCRIPTION = category_description;
		Status = status;
		//this.tech_id = tech_id;
		this.user_id = user_id;
		this.create_date = create_date;
	}
	public String getCATEGORY_ID() {
		return CATEGORY_ID;
	}
	public void setCATEGORY_ID(String category_id) {
		CATEGORY_ID = category_id;
	}
	public String getCATEGORY_NAME() {
		return CATEGORY_NAME;
	}
	public void setCATEGORY_NAME(String category_name) {
		CATEGORY_NAME = category_name;
	}
	public String getCATEGORY_DESCRIPTION() {
		return CATEGORY_DESCRIPTION;
	}
	public void setCATEGORY_DESCRIPTION(String category_description) {
		CATEGORY_DESCRIPTION = category_description;
	}
	public String getIS_DELETED() {
		return IS_DELETED;
	}
	public void setIS_DELETED(String is_deleted) {
		IS_DELETED = is_deleted;
	}
	public String getstatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}

	public String getTech_id() {
		return tech_id;
	}
	public void setTech_id(String tech_id) {
		this.tech_id = tech_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	
	
}
