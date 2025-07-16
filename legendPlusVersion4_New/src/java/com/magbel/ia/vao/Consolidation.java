
package com.magbel.ia.vao;

public class Consolidation{

	private String id;
	private String code;
	private String description;
	private String address;
	private String contactPerson;
	private String accountNo;
	private String bank;
	private String fax;
	private String personno;
	private String currency;
	private String status;
	private String sort;

	public Consolidation(String id,String code,String description,String address,String contactPerson,
				String accountNo,String bank,String fax,String personno,String currency,String status,String sort){

		setId(id);
		setCode(code);
		setDescription(description);
		setAddress(address);
		setContactPerson(contactPerson);
		setAccountNo(accountNo);
		setBank(bank);
		setFax(fax);
		setPersonno(personno);
		setCurrency(currency);
		setStatus(status);
		setSort(sort);

	}

	public void setId(String id){
		this.id = id;
	}
	public void setCode(String code){
		this.code = code;
	}
	public void setDescription(String description){
		this.description = description;
	}
	public void setAddress(String address){
		this.address = address;
	}
	public void setContactPerson(String contactPerson){
		this.contactPerson = contactPerson;
	}
	public void setAccountNo(String accountNo){
		this.accountNo = accountNo;
	}
	public void setBank(String bank){
		this.bank = bank;
	}
	public void setFax(String fax){
		this.fax = fax;
	}
	public void setPersonno(String personno){
		this.personno = personno;
	}
	public void setCurrency(String currency){
		this.currency = currency;
	}
	public void setStatus(String status){
		this.status = status;
	}
	public void setSort(String sort){
		this.sort = sort;
	}

	public String getId(){
		return this.id;
	}
	public String getCode(){
		return this.code;
	}
	public String getDescription(){
		return this.description;
	}
	public String getAddress(){
		return this.address;
	}
	public String getContactPerson(){
		return this.contactPerson;
	}
	public String getAccountNo(){
		return this.accountNo;
	}
	public String getBank(){
		return this.bank;
	}
	public String getFax(){
		return this.fax;
	}
	public String getPersonno(){
		return this.personno;
	}
	public String getCurrency(){
		return this.currency;
	}
	public String getStatus(){
		return this.status;
	}
	public String getSort(){
		return this.sort;
	}
}