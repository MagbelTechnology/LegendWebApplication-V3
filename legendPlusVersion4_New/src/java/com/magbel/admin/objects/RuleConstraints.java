package com.magbel.admin.objects;

public class RuleConstraints {
	private String criteria_ID;
	private String NAME;
	private String RESPONSE_DAY;
	private String RESPONSE_HOUR;
	private String RESPONSE_MINUTE;
	private String RESOLVE_DAY;
	private String RESOLVE_HOUR;
	private String RESOLVE_MINUTE;
	private String CONSTRAINT;
	private String CONSTRAINT2;
	public String getCONSTRAINT2() {
		return CONSTRAINT2;
	}
	public void setCONSTRAINT2(String constraint2) {
		CONSTRAINT2 = constraint2;
	}
	public RuleConstraints(String constraint2) {
		super();
		CONSTRAINT2 = constraint2;
	}
	private String itemName2;
	public RuleConstraints() {
		super();
	}
	public RuleConstraints(String criteria_ID, String name,
			String response_day, String response_hour, String response_minute,
			String resolve_day, String resolve_hour, String resolve_minute,
			String constraint) {
		super();
		this.criteria_ID = criteria_ID;
		NAME = name;
		RESPONSE_DAY = response_day;
		RESPONSE_HOUR = response_hour;
		RESPONSE_MINUTE = response_minute;
		RESOLVE_DAY = resolve_day;
		RESOLVE_HOUR = resolve_hour;
		RESOLVE_MINUTE = resolve_minute;
		CONSTRAINT = constraint;
	}
	
	public RuleConstraints(String criteria_ID, String name,
			String response_day, String response_hour, String response_minute,
			String resolve_day, String resolve_hour, String resolve_minute,
			String constraint, String itemName2) {
		super();
		this.criteria_ID = criteria_ID;
		NAME = name;
		RESPONSE_DAY = response_day;
		RESPONSE_HOUR = response_hour;
		RESPONSE_MINUTE = response_minute;
		RESOLVE_DAY = resolve_day;
		RESOLVE_HOUR = resolve_hour;
		RESOLVE_MINUTE = resolve_minute;
		CONSTRAINT = constraint;
		this.itemName2 = itemName2;
	}
	
	public String getItemName2() {
		return itemName2;
	}
	public void setItemName2(String itemName2) {
		this.itemName2 = itemName2;
	}
	public String getCriteria_ID() {
		return criteria_ID;
	}
	public void setCriteria_ID(String criteria_ID) {
		this.criteria_ID = criteria_ID;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String name) {
		NAME = name;
	}
	public String getRESPONSE_DAY() {
		return RESPONSE_DAY;
	}
	public void setRESPONSE_DAY(String response_day) {
		RESPONSE_DAY = response_day;
	}
	public String getRESPONSE_HOUR() {
		return RESPONSE_HOUR;
	}
	public void setRESPONSE_HOUR(String response_hour) {
		RESPONSE_HOUR = response_hour;
	}
	public String getRESPONSE_MINUTE() {
		return RESPONSE_MINUTE;
	}
	public void setRESPONSE_MINUTE(String response_minute) {
		RESPONSE_MINUTE = response_minute;
	}
	public String getRESOLVE_DAY() {
		return RESOLVE_DAY;
	}
	public void setRESOLVE_DAY(String resolve_day) {
		RESOLVE_DAY = resolve_day;
	}
	public String getRESOLVE_HOUR() {
		return RESOLVE_HOUR;
	}
	public void setRESOLVE_HOUR(String resolve_hour) {
		RESOLVE_HOUR = resolve_hour;
	}
	public String getRESOLVE_MINUTE() {
		return RESOLVE_MINUTE;
	}
	public void setRESOLVE_MINUTE(String resolve_minute) {
		RESOLVE_MINUTE = resolve_minute;
	}
	public String getCONSTRAINT() {
		return CONSTRAINT;
	}
	public void setCONSTRAINT(String constraint) {
		CONSTRAINT = constraint;
	}
	
	
}
