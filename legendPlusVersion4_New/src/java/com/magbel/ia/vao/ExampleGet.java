package com.magbel.ia.vao;

public class ExampleGet
{
	private String code;
	private String name;
	private String address;
	private String contact;
	private String phone;
	private String status;
	private String id;

		public ExampleGet(String code,String name,String address,String contact,String phone,String status,String id)
		{
		this.code=code;
		this.name=name;
		this.address=address;
		this.contact=contact;
		this.phone=phone;
		this.status=status;
		this.id=id;
		}
		
		public ExampleGet()
		{
		}
		
		public String getCode() 
		{
		return code;
		}
		public void setCode(String code) 
		{
		this.code = code;
		}
		
		public String getName() 
		{
		return name;
		}
		public void setName(String name) 
		{
		this.name = name;
		}
		
		public String getAddress() 
		{
		return address;
		}
		public void setAddress(String address) 
		{
		this.address = address;
		}
		
		public String getContact() 
		{
		return contact;
		}
		public void setContact(String contact) 
		{
		this.contact = contact;
		}
		
		public String getPhone() 
		{
		return phone;
		}
		public void setPhone(String phone) 
		{
		this.phone = phone;
		}
		
		public String getStatus() 
		{
		return status;
		}
		public void setStatus(String status) 
		{
		this.status = status;
		}
		
		public String getId() 
		{
		return this.id;
		}	
		public void setId(String id) 
		{
        this.id = id;
		}
	
	
}