package com.magbel.ia.vao;

/**
 * <p>
 * Title: fileName.java
 * </p>
 * 
 * <p>
 * Description: File Description
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: Magbel Technologies LTD
 * </p>
 * 
 * @author Rahman Oloritun
  * @update Bolanle M Sule.
 * @version 1.0
 */
public class Section {
	private String section_id;

	private String section_code;

	private String section_acronym;

	private String section_name;

	private String section_status;

	private String userid;
	
	private String companyCode;

	public Section() {
	}

	public Section(String section_id, String section_code,
				   String section_acronym, String section_name, String section_status,
				   String userid,String companyCode) {

		this.section_id = section_id;
		this.section_code = section_code;
		this.section_acronym = section_acronym;
		this.section_name = section_name;
		this.section_status = section_status;
		this.userid = userid;
		this.companyCode = companyCode;

	}

	public void setSection_id(String section_id) {
		this.section_id = section_id;
	}

	public void setSection_code(String section_code) {
		this.section_code = section_code;
	}

	public void setSection_acronym(String section_acronym) {
		this.section_acronym = section_acronym;
	}

	public void setSection_name(String section_name) {
		this.section_name = section_name;
	}

	public void setSection_status(String section_status) {
		this.section_status = section_status;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getSection_id() {
		return section_id;
	}

	public String getSection_code() {
		return section_code;
	}

	public String getSection_acronym() {
		return section_acronym;
	}

	public String getSection_name() {
		return section_name;
	}

	public String getSection_status() {
		return section_status;
	}

	public String getUserid() {
		return userid;
	}
	
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCompanyCode() {
		return companyCode;
	}
}
