package com.magbel.ia.vao;

/**
 * <p>Title: BranchDept.java</p>
 *
 * <p>Description: Object of Relationship btw dept and Branches</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: Magbel Technologies LTD</p>
 *
 * @author Rahman Oloritun
 * @version 1.0
 */
public class BranchDept {
    private String branchCode;
    private String deptCode;
    private String gl_prefix;
    private String gl_suffix;
    private String branchId;
    private String deptId;
    private String mtid;
    private String compCode;
    public String getCompCode() {
		return compCode;
	}

	public void setCompCode(String compCode) {
		this.compCode = compCode;
	}

	public BranchDept() {
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public void setGl_prefix(String gl_prefix) {
        this.gl_prefix = gl_prefix;
    }

    public void setGl_suffix(String gl_suffix) {
        this.gl_suffix = gl_suffix;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public void setMtid(String mtid) {
        this.mtid = mtid;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public String getGl_prefix() {
        return gl_prefix;
    }

    public String getGl_suffix() {
        return gl_suffix;
    }

    public String getBranchId() {
        return branchId;
    }

    public String getDeptId() {
        return deptId;
    }

    public String getMtid() {
        return mtid;
    }


}
