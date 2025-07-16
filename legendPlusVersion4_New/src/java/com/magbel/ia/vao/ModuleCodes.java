package com.magbel.ia.vao;

public class ModuleCodes {
	private String moduleName;
	
	private String moduleAbv;

	private String is_prefix;

	private int startNo;

	/**
	 * @return the is_prefix
	 */
	public String getIs_prefix() {
		return is_prefix;
	}

	/**
	 * @param is_prefix the is_prefix to set
	 */
	public void setIs_prefix(String is_prefix) {
		this.is_prefix = is_prefix;
	}

	/**
	 * @return the moduleAbv
	 */
	public String getModuleAbv() {
		return moduleAbv;
	}

	/**
	 * @param moduleAbv the moduleAbv to set
	 */
	public void setModuleAbv(String moduleAbv) {
		this.moduleAbv = moduleAbv;
	}

	/**
	 * @return the moduleName
	 */
	public String getModuleName() {
		return moduleName;
	}

	/**
	 * @param moduleName the moduleName to set
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	/**
	 * @return the startNo
	 */
	public int getStartNo() {
		return startNo;
	}

	/**
	 * @param startNo the startNo to set
	 */
	public void setStartNo(int startNo) {
		this.startNo = startNo;
	}

	/**
	 * 
	 */
	public ModuleCodes() {
		//super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param moduleName
	 * @param moduleAbv
	 * @param is_prefix
	 * @param startNo
	 */
	public ModuleCodes(String moduleName, String moduleAbv, String is_prefix, int startNo) {
		this.moduleName = moduleName;
		this.moduleAbv = moduleAbv;
		this.is_prefix = is_prefix;
		this.startNo = startNo;
	}
	
	
	
	
}
