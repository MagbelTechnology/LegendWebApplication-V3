
package com.magbel.ia.vao;

public class TransactionCode
{
	private String id;
	private String code;
	private String description;
	private String statusCode;
	private String companyCode;

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public TransactionCode(String id, String code, String description)
	{
		setId(id);
		setCode(code);
		setDescription(description);
	}
	
	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public TransactionCode()
	{
	
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

	public String getId(){
		return this.id;
	}
	public String getCode(){
		return this.code;
	}
	public String getDescription(){
		return this.description;
	}
}