

package com.magbel.ia.vao;

public class CalendarDate{
	
	private String id;
	private String startDate;
	private String endDate;
	private String period;
	
	public CalendarDate(String id, String startDate, String endDate, String period){
		
		setId(id);
		setStartDate(startDate);
		setEndDate(endDate);
		setPeriod(period);
		
	}
	
	public void setId(String id){
		this.id = id;
	}
	public void setStartDate(String startDate){
		this.startDate = startDate;
	}
	public void setEndDate(String endDate){
		this.endDate = endDate;
	}
	public void setPeriod(String period){
		this.period = period;
	}
	
	public String getId(){
		return this.id;
	}
	public String getStartDate(){
		return this.startDate;
	}
	public String getEndDate(){
		return this.endDate;
	}
	public String getPeriod(){
		return this.period;
	}
}