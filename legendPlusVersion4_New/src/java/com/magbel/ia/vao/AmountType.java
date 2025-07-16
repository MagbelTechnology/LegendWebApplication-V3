package com.magbel.ia.vao;

public class AmountType
{
	private String gl_acount;
	private String ref_number;
	
public AmountType()
 	{}

	public AmountType(String gl_acount,String ref_number)
       {
		super();
		this.gl_acount =gl_acount ;
		this.ref_number=ref_number;
       }
	public AmountType(String id,String amountcode, int UserId, String description, String Type)
    {
		super();
		this.gl_acount =gl_acount ;
		this.ref_number=ref_number;
    }
	public String getGlaccount()
		{
		return gl_acount ;
		}
	public void setGlaccount(String gl_acount )
		{
		this.gl_acount =gl_acount ;
		}
		
		
		public String getRefnumber()
		{
		return ref_number;
		}
		public void setRefnumber(String ref_number)
		{
		this.ref_number=ref_number;
		}
}