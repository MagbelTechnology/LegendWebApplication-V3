package com.magbel.ia.vao;

import java.io.Serializable;


public class Contact implements Serializable 
{
	 
	private static final long serialVersionUID = 1L;
	
    private  String mtId;
    private  String  contactCode;
	 private  String  customerNo;
	 private  String  firstName;
	 private  String  lastName;
	 private  String  middleName;
  private  String  contactFullName;
    private  String  homePhone;
    private  String  homePhoneExt;
    private  String  businessPhone;
    private  String  businessPhoneExt;
    private  String  faxNo;
    private  String  mobilePhone;
    private  String  postalCode;
    private  String  homeEmailAddress;
    private  String  businessEmailAddress;
    private  int  userId = 0;
    
    
    public  Contact(){}
    
    	public final String getBusinessEmailAddress() {
			return businessEmailAddress;
		}
		
		
		public final void setBusinessEmailAddress(String businessEmailAddress) {
			this.businessEmailAddress = businessEmailAddress;
		}
		
		
		public final String getBusinessPhone() {
			return businessPhone;
		}
		
		
		public final void setBusinessPhone(String businessPhone) {
			this.businessPhone = businessPhone;
		}
		
		
		public final String getBusinessPhoneExt() {
			return businessPhoneExt;
		}
		
		
		public final void setBusinessPhoneExt(String businessPhoneExt) {
			this.businessPhoneExt = businessPhoneExt;
		}
		
		
		public final String getContactCode() {
			return contactCode;
		}
		
		
		public final void setContactCode(String contactCode) {
			this.contactCode = contactCode;
		}
		
		
		public final String getFirstName() {
			return firstName;
		}
		
		
		public final void setFirstName(String  FirstName) {
			this.firstName = FirstName;
		}
		
		
		public final String getFullName() {
			return contactFullName;
		}
		
		
		public final void setContactFullName(String contactFullName) {
			this.contactFullName = contactFullName;
		}
		
		
		public final String getLastName() {
			return  lastName;
		}
		
		
		public final void setLastName(String LastName) {
			this.lastName = LastName;
		}
		
		
		public final String getMiddleName() {
			return middleName;
		}
		
		
		public final void setMiddleName(String MiddleName) {
			this.middleName = MiddleName;
		}
		
		
		public final String getCustomerNo() {
			return customerNo;
		}
		
		
		public final void setCustomerNo(String customerNo) {
			this.customerNo = customerNo;
		}
		
		
		public final String getFaxNo() {
			return faxNo;
		}
		
		
		public final void setFaxNo(String faxNo) {
			this.faxNo = faxNo;
		}
		
		
		public final String getHomeEmailAddress() {
			return homeEmailAddress;
		}
		
		
		public final void setHomeEmailAddress(String homeEmailAddress) {
			this.homeEmailAddress = homeEmailAddress;
		}
		
		
		
		public final String getHomePhone() {
			return homePhone;
		}
		
		
		public final void setHomePhone(String homePhone) {
			this.homePhone = homePhone;
		}
		
		
		public final String getHomePhoneExt() {
			return homePhoneExt;
		}
		
		
		public final void setHomePhoneExt(String homePhoneExt) {
			this.homePhoneExt = homePhoneExt;
		}
		
		
		public final String getMobilePhone() {
			return mobilePhone;
		}
		
		
		public final void setMobilePhone(String mobilePhone) {
			this.mobilePhone = mobilePhone;
		}
		
		public final String getMtId() {
			return this.mtId;
		}
		
		
		public final void setMtId(String mtId) {
			this.mtId = mtId;
		}
		
		
		public final String getPostalCode() {
			return postalCode;
		}
		
		
		public final void setPostalCode(String postalCode) {
			this.postalCode = postalCode;
		}
		
		
		public final int getUserId() {
			return userId;
		}
		
		
		public final void setUserId(int userId) {
			this.userId = userId;
		}
	    
	    
 

}
