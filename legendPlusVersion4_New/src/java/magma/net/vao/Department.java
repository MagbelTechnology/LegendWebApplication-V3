package magma.net.vao;


public class Department {
    private String deptId;
    private String deptCode;
    private String deptName;
    private String deptAcronym;
    private String branch;
    private String glPrefix;
    private String deptStatus;
    private String userId;
    private String createDate;

    public Department(String deptId, String deptCode, String deptName,
                      String deptAcronym,
                      String branch, String glPrefix, String deptStatus,
                      String userId, String createDate) {
        setDeptId(deptId);
        setDeptCode(deptCode);
        setDeptName(deptName);
        setDeptAcronym(deptAcronym);
        setBranch(branch);
        setGlPrefix(glPrefix);
        setDeptStatus(deptStatus);
        setUserId(userId);
        setCreateDate(createDate);
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptAcronym() {
        return deptAcronym;
    }

    public void setDeptAcronym(String deptAcronym) {
        this.deptAcronym = deptAcronym;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getGlPrefix() {
        return glPrefix;
    }

    public void setGlPrefix(String glPrefix) {
        this.glPrefix = glPrefix;
    }

    public String getDeptStatus() {
        return deptStatus;
    }

    public void setDeptStatus(String deptStatus) {
        this.deptStatus = deptStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}