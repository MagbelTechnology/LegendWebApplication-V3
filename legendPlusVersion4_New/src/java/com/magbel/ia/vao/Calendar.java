package com.magbel.ia.vao;


/**
 * <p>Title: filestartDate.java</p>
 *
 * <p>Description: File Description</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Bolanle M. Sule
 * @version 1.0
 */
public class Calendar {

	private String id;
	private String startDate;
	private String endDate;
    private String qtrEnd;
	private String period;
	private String status;
	private String weekday;
	private String calendarDate;
	private String processstatus;
	private String holiday;
	private String day;
	private String month;
	private String year;
	private String closestatus;

    public Calendar(String id,String startDate,String endDate, String qtrEnd,
    		        String period, String status) {
    			
		setId(id);
		setStartDate(startDate);
		setEndDate(endDate);
		setQtrEnd(qtrEnd);
		setPeriod(period);
		setStatus(status);
		
    }
    public Calendar(String id,String weekday,String calendarDate, String endDate, String processstatus,
	        String holiday, String day, String month, String year,  String closestatus) {
		
		setId(id);
		setWeekday(weekday);
		setCalendarDate(calendarDate);
		setEndDate(endDate);
		setProcessstatus(processstatus);
		setHoliday(holiday);
		setDay(day);
		setMonth(month);
		setYear(year);
		setClosestatus(closestatus);
		
		}
    
	public void setId(String id) {
        this.id = id;
    }
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

    public void setQtrEnd(String qtrEnd){
		this.qtrEnd = qtrEnd;
	}
	
	public void setPeriod(String period) {
		this.period = period;
    }
	
	public void setStatus(String status) {
		this.status = status;
    }
	
	public void setWeekday(String weekday) {
		this.weekday = weekday;
    }
	
	public void setCalendarDate(String calendarDate) {
		this.calendarDate = calendarDate;
    }
	
	public void setProcessstatus(String processstatus) {
		this.processstatus = processstatus;
    }
	
	public void setHoliday(String holiday) {
		this.holiday = holiday;
    }
	
	public void setDay(String day) {
		this.day = day;
    }
	
	public void setMonth(String month) {
		this.month = month;
    }
	
	public void setYear(String year) {
		this.year = year;
    }
	
	public void setClosestatus(String closestatus) {
		this.closestatus = closestatus;
    }	

	public String getId() {
		return this.id;
    }
	
    public String getStartDate(){
		return this.startDate;
	}

    public String getEndDate() {
		return this.endDate;
	}

    public String getQtrEnd() {
        return this.qtrEnd;
    }

	public String getPeriod() {
	    return this.period;
    }
	
	public String getStatus() {
	    return this.status;
    }

	public String getWeekday() {
	    return this.weekday;
    }
	
	public String getCalendarDate() {
	    return this.calendarDate;
    }	

	public String getProcessstatus() {
	    return this.processstatus;
    }
	
	public String getHoliday() {
	    return this.holiday;
    }

	public String getDay() {
	    return this.day;
    }
	
	public String getMonth() {
	    return this.month;
    }	
	
	public String getYear() {
	    return this.year;
    }
	
	public String getClosestatus() {
	    return this.closestatus;
    }	
	
}
