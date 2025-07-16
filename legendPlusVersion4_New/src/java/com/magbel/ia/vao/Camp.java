
package com.magbel.ia.vao;

public class Camp{

	private String id;
	private String code;
	private String status;

	public Camp() {
		// TODO Auto-generated constructor stub
	}
	
	public Camp(String id, String code, String status){

		setId(id);
		setCode(code);
		setStatus(status);
		
	}

	public void setId(String id){
		this.id = id;
	}
	public void setCode(String code){
		this.code = code;
	}
	public void setStatus(String status){
		this.status = status;
	}
	
	public String getId(){
		return this.id;
	}
	public String getCode(){
		return this.code;
	}
	public String getStatus(){
		return this.status;
	}
	
}