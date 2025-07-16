package com.magbel.ia.vao;
/**
 * <p>Title: ClassPrivilege.java</p>
 *
 * <p>Description: ClassPrivilege </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Rahman Oloritun
 * @version 1.0
 */
public class ClassPrivilege {

	private String clss_uuid;

	private String role_uuid;

	private String role_view;

	private String role_addn;

	private String role_edit;

	public ClassPrivilege() {
		// TODO Auto-generated constructor stub
	}

	public ClassPrivilege(String clss_uuid, String role_uuid, String role_view,
			String role_addn, String role_edit) {
		// TODO Auto-generated constructor stub
		this.clss_uuid = clss_uuid;
		this.role_uuid = role_uuid;
		this.role_view = role_view;
		this.role_addn = role_addn;
		this.role_edit = role_edit;

	}

	/**
	 * @param clss_uuid
	 *            the clss_uuid to set
	 */
	public void setClss_uuid(String clss_uuid) {
		this.clss_uuid = clss_uuid;
	}

	/**
	 * @return the clss_uuid
	 */
	public String getClss_uuid() {
		return clss_uuid;
	}

	/**
	 * @param role_uuid
	 *            the role_uuid to set
	 */
	public void setRole_uuid(String role_uuid) {
		this.role_uuid = role_uuid;
	}

	/**
	 * @return the role_uuid
	 */
	public String getRole_uuid() {
		return role_uuid;
	}

	/**
	 * @param role_view
	 *            the role_view to set
	 */
	public void setRole_view(String role_view) {
		this.role_view = role_view;
	}

	/**
	 * @return the role_view
	 */
	public String getRole_view() {
		return role_view;
	}

	/**
	 * @param role_addn
	 *            the role_addn to set
	 */
	public void setRole_addn(String role_addn) {
		this.role_addn = role_addn;
	}

	/**
	 * @return the role_addn
	 */
	public String getRole_addn() {
		return role_addn;
	}

	/**
	 * @param role_edit
	 *            the role_edit to set
	 */
	public void setRole_edit(String role_edit) {
		this.role_edit = role_edit;
	}

	/**
	 * @return the role_edit
	 */
	public String getRole_edit() {
		return role_edit;
	}

}
