package com.magbel.legend.vao;

/**
 * <p>
 * Title: Company.java
 * </p>
 *
 * <p>
 * Description: Company
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2020
 * </p>
 *
 * <p>
 * Company: Magbel Technologies LTD
 * </p>
 *
 * @author Lekan Matanmi.
 * @version 3.0
 */

public class SendMail {


        private int id = 0;
        private String address = "";
        private String header = "";
	    private String  body="";  
        private String  status="";
        private String  itemType="";
        private String  createDate="";
        
    public SendMail() {  
    }
    
   public SendMail(int id,String createDate,String address,String header,String body,String status) {
   this.id=id;
   this.createDate = createDate;
   this.address = address;
   this.header  = header;
   this.body = body;
   this.status = status;
   
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}	

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}	
	
}
