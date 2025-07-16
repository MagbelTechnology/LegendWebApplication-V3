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
public class Department {

	private String dept_id;

	private String dept_code;

	private String dept_name;

	private String dept_acronym;

	private String dept_status;

	private String user_id;
	
	private String companyCode;

	public Department() {
	}

	public Department(String dept_id, String dept_code, String dept_name,
					  String dept_acronym, String dept_status, String user_id, String companyCode) {
			
		this.dept_id = dept_id;
		this.dept_code = dept_code;
		this.dept_name = dept_name;
		this.dept_acronym = dept_acronym;
		this.dept_status = dept_status;
		this.user_id = user_id;
		this.companyCode = companyCode;

	}

	public void setDept_id(String dept_id) {
		this.dept_id = dept_id;
	}

	public void setDept_code(String dept_code) {
		this.dept_code = dept_code;
	}

	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}

	public void setDept_acronym(String dept_acronym) {
		this.dept_acronym = dept_acronym;
	}

	public void setDept_status(String dept_status) {
		this.dept_status = dept_status;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getDept_id() {
		return dept_id;
	}

	public String getDept_code() {
		return dept_code;
	}

	public String getDept_name() {
		return dept_name;
	}

	public String getDept_acronym() {
		return dept_acronym;
	}

	public String getDept_status() {
		return dept_status;
	}

	public String getUser_id() {
		return user_id;
	}
	
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCompanyCode() {
		return companyCode;
	}

}
