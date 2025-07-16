package com.magbel.ia.vao;


public class ImprestRef {

	private String mtId;

	private String refNumber;

	
	public ImprestRef(String mtId, String refNumber) 
{
		this.mtId = mtId;
		this.refNumber = refNumber;
		
	}

	
	public ImprestRef() {
		
	}

	public String getMtId() 
        {
		return mtId;
	}


	public void setMtId(String mtId) 
        {
		this.mtId = mtId;
	}


	public String getRefNumber() 
        {
		return refNumber;
	}


	public void setRefNumber(String refNumber) 
       {
		this.refNumber = refNumber;
	}

}
