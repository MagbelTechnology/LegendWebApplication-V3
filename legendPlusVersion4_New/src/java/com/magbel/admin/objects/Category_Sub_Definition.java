package com.magbel.admin.objects;

public class Category_Sub_Definition {
	
	private String CATEGORY_ID;
	private String SUB_CATEGORY_ID;
	private String SUB_CATEGORY_NAME;
	private String SUB_CATEGORY_DESCRIPTION;
	private String IS_DELETED;
	private String tech_id;
	private String user_id;
	private String Status;
	private String create_date; 
	public Category_Sub_Definition(String category_id, String sub_category_id,
			String sub_category_name, String sub_category_description,
			String status, String user_id,
			String create_date) {
		super();
		CATEGORY_ID = category_id;
		SUB_CATEGORY_ID = sub_category_id;
		SUB_CATEGORY_NAME = sub_category_name;
		SUB_CATEGORY_DESCRIPTION = sub_category_description;
		Status = status;
		//this.Status = status;
		this.user_id = user_id;
		this.create_date = create_date;
	
	}
	public Category_Sub_Definition(String category_id, String sub_category_id,
			String sub_category_name, String sub_category_description,
			String status, String user_id) {
		this.CATEGORY_ID = category_id;
		this.SUB_CATEGORY_ID = sub_category_id;
		this.SUB_CATEGORY_NAME = sub_category_name;
		this.SUB_CATEGORY_DESCRIPTION = sub_category_description;
		this.Status = status;
		this.user_id = user_id;
	
	}
	public Category_Sub_Definition() {
		super();
	}
	public String getCATEGORY_ID() {
		return CATEGORY_ID;
	}
	public void setCATEGORY_ID(String category_id) {
		CATEGORY_ID = category_id;
	}
	public String getSUB_CATEGORY_ID() {
		return SUB_CATEGORY_ID;
	}
	public void setSUB_CATEGORY_ID(String sub_category_id) {
		SUB_CATEGORY_ID = sub_category_id;
	}
	public String getSUB_CATEGORY_NAME() {
		return SUB_CATEGORY_NAME;
	}
	public void setSUB_CATEGORY_NAME(String sub_category_name) {
		SUB_CATEGORY_NAME = sub_category_name;
	}
	public String getSUB_CATEGORY_DESCRIPTION() {
		return SUB_CATEGORY_DESCRIPTION;
	}
	public void setSUB_CATEGORY_DESCRIPTION(String sub_category_description) {
		SUB_CATEGORY_DESCRIPTION = sub_category_description;
	}
	public String getIS_DELETED() {
		return IS_DELETED;
	}
	public void setIS_DELETED(String is_deleted) {
		IS_DELETED = is_deleted;
	}
	public String getTech_id() {
		return tech_id;
	}
	public void setStatus(String Status) {
		this.Status = Status;
	}
	public String getStatus() {
		return Status;
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
