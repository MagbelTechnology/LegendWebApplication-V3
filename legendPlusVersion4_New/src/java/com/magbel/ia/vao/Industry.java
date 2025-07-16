
package com.magbel.ia.vao;

public class Industry{

	private String id;
	private String code;
	private String name;

	public Industry(String id, String code, String name){

		setId(id);
		setCode(code);
		setName(name);

	}

	public void setId(String id){
		this.id = id;
	}
	public void setCode(String code){
		this.code = code;
	}
	public void setName(String name){
		this.name = name;
	}

	public String getId(){
		return this.id;
	}
	public String getCode(){
		return this.code;
	}
	public String getName(){
		return this.name;
	}
}