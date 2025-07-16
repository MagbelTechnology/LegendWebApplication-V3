
package com.magbel.ia.vao;

public class ErrorCode{

	private String id;
	private String code;
	private String description;

	public ErrorCode(String id, String code, String description){

		setId(id);
		setCode(code);
		setDescription(description);

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