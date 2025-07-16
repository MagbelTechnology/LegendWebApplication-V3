package com.magbel.admin.objects;

public class mail_setup {
	 	private String mailcode;
	 	
	 	private String mailheading;
	 	private String maildescription;
	 	
	 	private String mailaddress;
	 	  
	    private String date;
	    
	    private String trans_type;
	    
	    private String userid;
	    
	    private String status;
	    
	    private String category;
	    private String SubCategory;
	    private String mailtype;
	    private String helptype;
	  
	    
public mail_setup()
{


}
public mail_setup(String mailcode, String mailheading, String maildescription, String mailaddress, String date, String trans_type, String userid,String status,String category,String mailtype,String helptype)
{ 
		this.mailcode = mailcode;
		this.mailheading = mailheading;
		this.maildescription = maildescription;
		this.mailaddress =mailaddress;
		this.date = date;
		this.trans_type = trans_type;
		this.userid = userid;
		this.status = status;
		this.category = category;
		this.mailtype = mailtype;
		this.helptype = helptype;

}

public mail_setup(String mailcode, String mailheading, String maildescription, String trans_type, String status,String category,String SubCategory,String mailtype,String helptype)
{
		this.mailcode = mailcode;
		this.mailheading = mailheading;
		this.maildescription = maildescription;
		this.trans_type = trans_type;
		this.status = status;
		this.category = category;
		this.SubCategory = SubCategory;
		this.mailtype = mailtype;
		this.helptype = helptype;	
		
		


}
		
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public String getMailaddress() {
			return mailaddress;
		}
		public void setMailaddress(String mailaddress) {
			this.mailaddress = mailaddress;
		}
		public String getMailcode() {
			return mailcode;
		}
		public void setMailcode(String mailcode) {
			this.mailcode = mailcode;
		}
		public String getmailheading() {
			return mailheading;
		}
		public void setmailheading(String mailheading) {
			this.mailheading = mailheading;
		}
		public String getMaildescription() {
			return maildescription;
		}
		public void setMaildescription(String maildescription) {
			this.maildescription = maildescription;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getTrans_type() {
			return trans_type;
		}
		public void setTrans_type(String trans_type) {
			this.trans_type = trans_type;
		}
		public String getUserid() {
			return userid;
		}
		public void setUserid(String userid) {
			this.userid = userid;
		}
		public String getcategory() {
			return category;
		}
		public void setcategory(String category) {
			this.category = category;
		}
		public String getSubCategory() {
			return SubCategory;
		}
		public void setSubCategory(String SubCategory) {
			this.SubCategory = SubCategory;
		}
		
		public String getmailtype() {
			return mailtype;
		}
		public void setmailtype(String mailtype) {
			this.mailtype = mailtype;
		}
		public String gethelptype() {
			return helptype;
		}
		public void sethelptype(String helptype) {
			this.helptype = helptype;
		}
	  

}
