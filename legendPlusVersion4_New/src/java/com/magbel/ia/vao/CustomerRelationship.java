package com.magbel.ia.vao;

import java.io.Serializable;

public class CustomerRelationship implements Serializable 
{
	 
	private static final long serialVersionUID = 1L;

	  private  String mtId;
	  private   String  customerCode;
	  private  String  relationshipCode;
	   private  String  relationshipClassCode;
	      private  String  relationshipClass;
	  private  String  relationship;
	  private String  relDescription;
	  private  String  reciprocalDescription;
	  private  String  reciprocalRelationship;
	  private  String relationshipToName;
	  private  String  relationshipToCustomerCode;
	  private  int  userId = 0;
	  private  String status;
	
	  
	  
	  public  CustomerRelationship(){}
	  
	  /**
	  public CustomerRelationship(String MtId, String CustomerCode, String  RelationshipCode, 
	                           String RelationshipClassCode, String Relationship,
			                 String  RelationshipDescription,  String ReciprocalDescription,
			                   String  ReciprocalRelationship, String  RelationshipToName,
			                    String  RelationshipToCustomerCode,  int  UserId)
	  {
		  this.mtId =  MtId;
		  this.customerCode  =  CustomerCode.trim();
		  this.relationshipCode  =  RelationshipCode.trim();
		  this.relationshipClassCode  =  RelationshipClassCode.trim();
		  this.relationship = Relationship.trim();
		  this.relDescription  =  RelationshipDescription.trim();
		  this.reciprocalDescription  =  ReciprocalDescription.trim();
		  this.reciprocalRelationship  =  ReciprocalRelationship.trim();
		  this.relationshipToName = RelationshipToName.trim();
		  this.relationshipToCustomerCode = RelationshipToCustomerCode.trim();
		  this.userId =  UserId;
	  }
	  

	  
public void setCustomerRelationship(String MtId, String CustomerCode,  String  RelationshipCode,
                     String  RelationshipClassCode,  String Relationship, String  RelationshipDescription,  					String ReciprocalDescription,    String  ReciprocalRelationship,
					 String  RelationshipToName,  String  RelationshipToCustomerCode,  int  UserId)
{
this.mtId =  MtId;
this.customerCode  =  CustomerCode.trim();
this.relationshipCode  =  RelationshipCode.trim();
this.relationshipClassCode  =  RelationshipClassCode.trim();
this.relationship = Relationship.trim();
this.relDescription  =  RelationshipDescription.trim();
this.reciprocalDescription  =  ReciprocalDescription.trim();
this.reciprocalRelationship  =  ReciprocalRelationship.trim();
this.relationshipToName = RelationshipToName.trim();
this.relationshipToCustomerCode = RelationshipToCustomerCode.trim();
this.userId =  UserId;
}


**/

public final String getMtId() {
	return mtId;
}


public final void setMtId(String MtId) {
	this.mtId = MtId;
}


public final String getReciprocalDescription() {
	return reciprocalDescription;
}


public final void setReciprocalDescription(String ReciprocalDescription) {
	this.reciprocalDescription = ReciprocalDescription;
}


public final String getReciprocalRelationship() {
	return reciprocalRelationship;
}


public final void setReciprocalRelationship(String ReciprocalRelationship) {
	this.reciprocalRelationship = ReciprocalRelationship;
}


public final String getRelationshipCode() {
	return relationshipCode;
}


public final void setRelationshipCode(String RelationshipCode) {
	this.relationshipCode = RelationshipCode;
}


public final String getRelDescription() {
	return relDescription;
}


public final void setRelDescription(String RelDescription) {
	this.relDescription = RelDescription;
}


public final int getUserId() {
	return userId;
}


public final void setUserId(int UserId) {
	this.userId = UserId;
}

 
public final String getRelationshipToCustomerCode() {
	return relationshipToCustomerCode;
}
public final void setRelationshipToCustomerCode(
		String RelationshipToCustomerCode) {
	this.relationshipToCustomerCode = RelationshipToCustomerCode;
}
public final String getRelationshipToName() {
	return relationshipToName;
}
public final void setRelationshipToName(String RelationshipToName) {
	this.relationshipToName = RelationshipToName;
}


public final String getRelationship() {
	return relationship;
}

public final void setRelationship(String Relationship) {
	this.relationship = Relationship;
}

 
 
public  final  String  getCustomerCode()
{
  return   this.customerCode;
}
 
 
 
public final String  getRelationshipClassCode()
{
  return  this.relationshipClassCode;
}


public final void  setRelationshipClassCode(String RelationshipClassCode)
{
  this.relationshipClassCode  =  RelationshipClassCode;
}
  


public final String  getRelationshipClass()
{
  return  this.relationshipClass;
}


public final void  setRelationshipClass(String RelationshipClass)
{
  this.relationshipClass  =  RelationshipClass;
}

 
public  final  void  setCustomerCode(String  CustomerCode)
{
    this.customerCode = CustomerCode;
}
 
 
 public final  void  setStatus(String Status)
 {
    this.status   =  Status;
}


public final String  getStatus()
 {
   return  this.status;
 }

 
 
}
