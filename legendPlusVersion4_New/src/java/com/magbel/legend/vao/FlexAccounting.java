package com.magbel.legend.vao;

import org.json.JSONArray;

public class FlexAccounting {

	public String coreBankingRef;
	public String source;
	public String userId;
	public String branch;
	public JSONArray accEntryDetails;
	
	public String getCoreBankingRef() {
		return coreBankingRef;
	}
	public void setCoreBankingRef(String coreBankingRef) {
		this.coreBankingRef = coreBankingRef;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public JSONArray getAccEntryDetails() {
		return accEntryDetails;
	}
	public void setAccEntryDetails(JSONArray accEntryDetails) {
		this.accEntryDetails = accEntryDetails;
	}
	
	
	
	
	
}
