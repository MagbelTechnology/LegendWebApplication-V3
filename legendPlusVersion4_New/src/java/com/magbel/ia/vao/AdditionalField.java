
package com.magbel.ia.vao;

public class AdditionalField{

	private String id;
	private String name;

	public AdditionalField(String id, String name){

		setId(id);
		setName(name);

	}

	public void setId(String id){
		this.id = id;
	}
	public void setName(String name){
		this.name = name;
	}

	public String getId(){
		return this.id;
	}
	public String getName(){
		return this.name;
	}
}