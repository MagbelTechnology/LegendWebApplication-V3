
package com.magbel.ia.vao;

public class ProcessingDate{

	private String id;
	private String code;
	private String day;

	public ProcessingDate(String id, String code, String day){

		setId(id);
		setCode(code);
		setDay(day);

	}

	public void setId(String id){
		this.id = id;
	}
	public void setCode(String code){
		this.code = code;
	}
	public void setDay(String day){
		this.day = day;
	}

	public String getId(){
		return this.id;
	}
	public String getCode(){
		return this.code;
	}
	public String getDay(){
		return this.day;
	}
}