
package com.magbel.ia.vao;

public class Frequency{

	private String id;
	private String shortHand;
	private String description;

	public Frequency(String id, String shortHand, String description){

		setId(id);
		setShortHand(shortHand);
		setDescription(description);

	}

	public void setId(String id){
		this.id = id;
	}
	public void setShortHand(String shortHand){
		this.shortHand = shortHand;
	}
	public void setDescription(String description){
		this.description = description;
	}

	public String getId(){
		return this.id;
	}
	public String getShortHand(){
		return this.shortHand;
	}
	public String getDescription(){
		return this.description;
	}
}