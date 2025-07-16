
package com.magbel.ia.vao;

public class Holiday{

	private String id;
	private String code;
	private String name;
	private String date;

	public Holiday(String id, String code, String name, String date){

		setId(id);
		setCode(code);
		setName(name);
		setDate(date);

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
	public void setDate(String date){
		this.date = date;
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
	public String getDate(){
		return this.date;
	}
}