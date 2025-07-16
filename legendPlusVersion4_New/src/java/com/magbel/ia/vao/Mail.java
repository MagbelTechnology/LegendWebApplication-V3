package com.magbel.ia.vao;

public class Mail 
{
	private String Mail_code;
	private String Mail_description;
	private String tran_code;
	private String Mail_address;
	private String User_ID;
	private String Status;
	private String Company_Code;
	private String createDate;
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getMail_code() {
		return Mail_code;
	}
	public void setMail_code(String mail_code) {
		Mail_code = mail_code;
	}
	public String getMail_description() {
		return Mail_description;
	}
	public void setMail_description(String mail_description) {
		Mail_description = mail_description;
	}
	
	
	public String getTran_code() {
		return tran_code;
	}
	public void setTran_code(String tran_code) {
		this.tran_code = tran_code;
	}
	public String getMail_address() {
		return Mail_address;
	}
	public void setMail_address(String mail_address) {
		Mail_address = mail_address;
	}
	public String getUser_ID() {
		return User_ID;
	}
	public void setUser_ID(String user_ID) {
		User_ID = user_ID;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getCompany_Code() {
		return Company_Code;
	}
	public void setCompany_Code(String company_Code) {
		Company_Code = company_Code;
	}
}
