package com.magbel.admin.objects;

import java.util.Date;

public class Bank {
	private String bankCode;
	private String bankName;
	private String address;
	private String phone;
	private String email;
	private String user_id ;
	private String Fax;
	private String Domain;
	private String Acronym;
	private String Status;
    private String organContact;
	Date createDate;
	public Bank() {
		super();
	}
	public Bank(String bankCode, String bankName, String address, String phone,
			String email, String Fax, String Domain, String Acronym, String Status) {
		super();
		this.bankCode = bankCode;
		this.bankName = bankName;
		this.address = address;
		this.phone = phone;
		this.email = email;
		this.Fax = Fax;
		this.Domain = Domain;
		this.Acronym = Acronym;
		this.Status = Status;
	}
	
	public Bank(String bankCode, String bankName, String address, String phone,
			String email, String user_id, String Fax, String Domain, String Acronym, String Status, Date createDate,String organContact) {
		super();
		this.bankCode = bankCode;
		this.bankName = bankName;
		this.address = address;
		this.phone = phone;
		this.email = email;
		this.user_id = user_id;
		this.Fax = Fax;
		this.Domain = Domain;
		this.Acronym = Acronym;
		this.Status = Status;
		this.createDate = createDate;
		this.organContact = organContact;
	}
	
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankName() {   
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFax() {
		return Fax;
	} 
	public void setFax(String Fax) {
		this.Fax = Fax;
	}	
	public String getDomain() {
		return Domain;
	}
	public void setDomain(String Domain) {
		this.Domain = Domain;
	}	
	public String getAcronym() {
		return Acronym;
	}
	public void setAcronym(String Acronym) {
		this.Acronym = Acronym;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String Status) {
		this.Status = Status;
	}
    public void setorganContact(String organContact)
    {
        this.organContact = organContact;
    }           
    public String getorganContact()
    {
        return organContact;
    }
    
	
}
