package com.magbel.ia.vao;


import  java.io.Serializable;


public class CustomerIdentification  implements Serializable 
{
	 
	private static final long serialVersionUID = 1L;
	private String  mtId;
	private  String  customerCode;
	private  String  identificationCode;
	private String   identificationType;
	private  String  dateOfExpiry;
	private  String  dateOfIssue;
	private  String  cityOfIssuance;
	private  String  cityOfRegistration;
	private String   citizenship;
	private  String  coyRegNo;
	private  String  coyRegOffice;
	private  String  coyRegOfficeCode;
	private  int  userId = 0;
	
	
	public  CustomerIdentification(){}
	
	/**
	public  CustomerIdentification(String MtId,   String  CustomerCode, 
						String  IdentificationCode, String  IdentificationType, 
			             String  DateOfExpiry,  String  DateOfIssue,  String  CityOfIssuance,
			           String  CityOfRegistration,  String  Citizenship,   
			            String  CoyRegNo,  String  CoyRegOffice,  String CoyRegOfficeCode,  int UserId)
	{
		this.mtId =  MtId.trim();
		this.customerCode =  CustomerCode.trim();
		this.identificationCode =  IdentificationCode.trim();
		this.identificationType =  IdentificationType.trim();
		this.dateOfExpiry =  DateOfExpiry.trim();
		this.dateOfIssue =  DateOfIssue.trim();
		this.cityOfIssuance  =  CityOfIssuance.trim();
		this.cityOfRegistration  =  CityOfRegistration.trim();
		this.citizenship  =  Citizenship.trim();
		this.coyRegNo  =  CoyRegNo.trim();
		this.coyRegOffice =  CoyRegOffice.trim();
		this.coyRegOfficeCode =  CoyRegOfficeCode.trim();		
	}
	
	

public  void  setCustomerIdentification(String MtId,  String  CustomerCode,
              String  IdentificationCode,  String  IdentificationType,  
          String  DateOfExpiry,  String  DateOfIssue,  String  CityOfIssuance,
          String  CityOfRegistration,  String  Citizenship,   
           String  CoyRegNo,  String  CoyRegOffice,  String CoyRegOfficeCode,  int UserId)
{
this.mtId =  MtId.trim();
this.customerCode  =  CustomerCode.trim();
this.identificationCode =  IdentificationCode.trim();
this.identificationType =  IdentificationType.trim();
this.dateOfExpiry =  DateOfExpiry.trim();
this.dateOfIssue =  DateOfIssue.trim();
this.cityOfIssuance  =  CityOfIssuance.trim();
this.cityOfRegistration  =  CityOfRegistration.trim();
this.citizenship  =  Citizenship.trim();
this.coyRegNo  =  CoyRegNo.trim();
this.coyRegOffice =  CoyRegOffice.trim();
this.coyRegOfficeCode =  CoyRegOfficeCode.trim();		
}
**/	
	
	
	
	public static final long getSerialVersionUID() {
		return serialVersionUID;
	}

	public final String getCitizenship() {
		return citizenship;
	}

	public final void setCitizenship(String Citizenship) {
		this.citizenship = Citizenship;
	}

	public final String getCityOfIssuance() {
		return cityOfIssuance;
	}

	public final void setCityOfIssuance(String CityOfIssuance) {
		this.cityOfIssuance = CityOfIssuance;
	}

	public final String getCityOfRegistration() {
		return cityOfRegistration;
	}

	public final void setCityOfRegistration(String CityOfRegistration) {
		this.cityOfRegistration = CityOfRegistration;
	}

	public final String getCompanyRegNo() {
		return coyRegNo;
	}

	public final void setCompanyRegNo(String CoyRegNo) {
		this.coyRegNo = CoyRegNo;
	}

	public final String getCompanyRegOffice() {
		return coyRegOffice;
	}

	public final void setCompanyRegOffice(String CoyRegOffice) {
		this.coyRegOffice = CoyRegOffice;
	}

	public final String getCoyRegOfficeCode() {
		return coyRegOfficeCode;
	}

	public final void setCoyRegOfficeCode(String CoyRegOfficeCode) {
		this.coyRegOfficeCode = CoyRegOfficeCode;
	}

	public final String getDateOfExpiration() {
		return dateOfExpiry;
	}

	public final void setDateOfExpiration(String DateOfExpiry) {
		this.dateOfExpiry = DateOfExpiry;
	}

	public final String getDateOfIssue() {
		return dateOfIssue;
	}

	public final void setDateOfIssue(String DateOfIssue) {
		this.dateOfIssue = DateOfIssue;
	}

	public final String getIdentificationCode() {
		return identificationCode;
	}

	public final void setIdentificationCode(String IdentificationCode) {
		this.identificationCode = IdentificationCode;
	}



	public final String getIdentificationType() {
		return identificationType;
	}

	public final void setIdentificationType(String IdentificationType) {
		this.identificationType = IdentificationType;
	}

	public final String getMtId() {
		return mtId;
	}

	public final void setMtId(String MtId) {
		this.mtId = MtId;
	}



	public final int getUserId() {
		return userId;
	}

	public final void setUserId(int UserId) {
		this.userId = UserId;
	}
	
	
	
	
	public final String getCustomerCode() {
	return customerCode;
}


public final void setCustomerCode(String  CustomerCode) {
	this.customerCode =  CustomerCode;
}

}
