package com.magbel.admin.objects;

public class lost_password {
	 	
	    private String date;
	    
	 	private String UserName;
	 	
	 	private String PhoneNo;
	 	private String email;

public lost_password()
{


}
public lost_password(String UserName,String PhoneNo)
{
		this.UserName = UserName;
		this.PhoneNo = PhoneNo;
}
		
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public String getUserName() {
			return UserName;
		}
		public void setUserName(String UserName) {
			this.UserName = UserName;
		}
		public String getPhoneNo() {
			return PhoneNo;
		}
		public void setPhoneNo(String PhoneNo) {
			this.PhoneNo = PhoneNo;
		}
		public String getemail() {
			return email;
		}
		public void setemail(String email) {
			this.email = email;
		} 
	  

}
