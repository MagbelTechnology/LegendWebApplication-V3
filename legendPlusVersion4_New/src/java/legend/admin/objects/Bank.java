package legend.admin.objects;

import java.util.Date;

public class Bank {
	private String bankCode;
	private String bankName;
	private String address;
	private String phone;
	private String email;
	private String user_id ;
	Date createDate;
	public Bank() {
		super();
	}
	public Bank(String bankCode, String bankName, String address, String phone,
			String email) {
		super();
		this.bankCode = bankCode;
		this.bankName = bankName;
		this.address = address;
		this.phone = phone;
		this.email = email;
	}
	
	public Bank(String bankCode, String bankName, String address, String phone,
			String email, String user_id, Date createDate) {
		super();
		this.bankCode = bankCode;
		this.bankName = bankName;
		this.address = address;
		this.phone = phone;
		this.email = email;
		this.user_id = user_id;
		this.createDate = createDate;
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
	
	
	
}
