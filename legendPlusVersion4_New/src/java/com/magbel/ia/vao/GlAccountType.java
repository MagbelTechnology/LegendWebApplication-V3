package com.magbel.ia.vao;
/**
 * <p>
 * com.magbel.ia.vao.ErrorCode.java Mar 26, 2007
 * </p>
 *
 * @author Morgan
 *
 * @version 1.00
 *
 */
public class GlAccountType {

	private String id;
	private String code;
	private String name;

	public GlAccountType() {

	}

	public GlAccountType(String id, String code,String name) {

	this.id = id;
	this.code = code;
	this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}