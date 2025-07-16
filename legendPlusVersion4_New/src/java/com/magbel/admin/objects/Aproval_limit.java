package com.magbel.admin.objects;

public class Aproval_limit{

	private String code;
	private double minAmt;
	private double maxAmt;
	private String desc;
	
	public Aproval_limit()
	{
		
		
	}
	public Aproval_limit(String code,double minAmt,double maxAmt,String desc)
	{
		this.code = code;
		this.minAmt = minAmt;
		this.maxAmt =maxAmt;
		this.desc = desc;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public double getMaxAmt() {
		return maxAmt;
	}
	public void setMaxAmt(double maxAmt) {
		this.maxAmt = maxAmt;
	}
	public double getMinAmt() {
		return minAmt;
	}
	public void setMinAmt(double minAmt) {
		this.minAmt = minAmt;
	}
	
   
}